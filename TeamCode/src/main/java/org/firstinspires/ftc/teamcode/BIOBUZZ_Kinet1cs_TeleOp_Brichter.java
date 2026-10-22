/* Copyright (c) 2025 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/*
 * Kinet1cs TeleOp
 * Controls (gamepad1):
 *   Left stick      = drive (field relative)
 *   Right stick X   = turn
 *   Left bumper     = hold for robot relative drive
 *   Right trigger   = intake in
 *   Left trigger    = intake reverse (spit out)
 *   Y               = toggle outtake flywheel on/off
 *   Right bumper    = fire (transfer feeds ONLY when flywheel is at speed)
 *   A               = reset IMU yaw (point robot "forward" first)
 */
@Configurable
@TeleOp(name = "Kinet1cs Bare Bones Teleop", group = "Robot")
public class BIOBUZZ_Kinet1cs_TeleOp_Brichter extends OpMode {

    // ---------- HARDWARE ----------
    DcMotorEx fL, fR, bL, bR;      // mecanum drive motors
    DcMotorEx intakeMain;          // main intake roller
    DcMotorEx outtake;             // flywheel launcher
    CRServo transfer;              // feeds elements into the flywheel
    CRServo fRIntake, fLIntake;    // side intake servos
    IMU imu;                       // gives robot heading for field relative drive

    ElapsedTime loopTimer = new ElapsedTime();  // measures how long each loop takes

    // ---------- TUNABLE VALUES (editable live in Panels because of @Configurable) ----------
    public static double intakePower = 0.3;
    public static double outtakeVelocity = 1500;   // ticks per second. Old value 10 was almost stopped
    public static double atSpeedTolerance = 0.95;  // flywheel counts as "ready" at 95% of target
    public static double transferPower = 0.2;
    public static double intakeServosPower = 0.3;
    public static double maxSpeed = 1.0;           // lower this for outreach / new drivers

    // ---------- STATE ----------
    boolean outtakeOn = false;  // is the flywheel toggled on
    boolean lastY = false;      // Y button state last loop, used to detect a single press

    @Override
    public void init() {
        // Configuration match up & setup in beggining
        fL = hardwareMap.get(DcMotorEx.class, "fL");
        fR = hardwareMap.get(DcMotorEx.class, "fR");
        bL = hardwareMap.get(DcMotorEx.class, "bL");
        bR = hardwareMap.get(DcMotorEx.class, "bR");
        intakeMain = hardwareMap.get(DcMotorEx.class, "intake");
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");
        transfer = hardwareMap.get(CRServo.class, "transfer");
        fRIntake = hardwareMap.get(CRServo.class, "fRIntake");
        fLIntake = hardwareMap.get(CRServo.class, "fLIntake");

        // This reverses fL and bR. If strafing/turning is wrong, change bR to bL.
        fL.setDirection(DcMotor.Direction.REVERSE);
        bR.setDirection(DcMotor.Direction.REVERSE);
        intakeMain.setDirection(DcMotor.Direction.FORWARD); // flip if intake spins backwards
        outtake.setDirection(DcMotor.Direction.FORWARD);    // flip if flywheel spins backwards
        fLIntake.setDirection(DcMotorSimple.Direction.FORWARD);

        // RUN_USING_ENCODER = motor uses its encoder to hold a steady speed.
        // BRAKE = drive motors stop right away when the stick is released.
        for (DcMotorEx m : new DcMotorEx[]{fL, fR, bL, bR, intakeMain, outtake}) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        // FLOAT for the flywheel so it coasts down instead of slamming to a stop
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // IMU setup. Change these two if the Control Hub is mounted differently.
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
        imu.resetYaw();  // whatever way the robot faces at init = "forward"
    }

    @Override
    public void loop() {
        // ---------- IMU RESET ----------
        if (gamepad1.a) imu.resetYaw();

        // ---------- INTAKE ----------
        // Right trigger pulls in, left trigger spits out, neither = stop.
        double intake = 0;
        if (gamepad1.right_trigger > 0.5) intake = intakePower;
        else if (gamepad1.left_trigger > 0.5) intake = -intakePower;
        intakeMain.setPower(intake);
        // Side servos follow the main intake direction. Left is negative because it's mirrored.
        fRIntake.setPower(Math.signum(intake) * intakeServosPower); // signum checks for positivity
        fLIntake.setPower(-Math.signum(intake) * intakeServosPower);

        // ---------- OUTTAKE ----------
        // Toggle only on the moment Y is pressed, not every loop it's held.
        if (gamepad1.y && !lastY) outtakeOn = !outtakeOn;
        lastY = gamepad1.y;
        outtake.setVelocity(outtakeOn ? outtakeVelocity : 0);

        // ---------- TRANSFER ----------
        // atSpeed check: only feed when the flywheel is fast enough or shots fall short.
        boolean atSpeed = outtakeOn && outtake.getVelocity() >= outtakeVelocity * atSpeedTolerance;
        transfer.setPower(gamepad1.right_bumper && atSpeed ? transferPower : 0);

        // ---------- DRIVE ----------
        // Stick Y is negative when pushed forward so flip it.
        if (gamepad1.left_bumper) {
            drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        } else {
            driveFieldRelative(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }

        // ---------- DEBUG TELEMETRY ----------
        telemetry.addData("Yaw (deg)", "%.1f", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        telemetry.addData("Outtake", "%s  %.0f / %.0f", outtakeOn ? "ON" : "OFF", outtake.getVelocity(), outtakeVelocity);
        telemetry.addData("At speed", atSpeed);
        telemetry.addData("Intake power", "%.2f", intake);
        telemetry.addData("Drive", "fL %.2f fR %.2f bL %.2f bR %.2f",
                fL.getPower(), fR.getPower(), bL.getPower(), bR.getPower());
        telemetry.addData("Loop (ms)", "%.1f", loopTimer.milliseconds()); // should stay around 20 ms or less
        loopTimer.reset();
        telemetry.update();
    }

    // Field relative drive: rotates the joystick direction by the robot's heading,
    // so forward on the stick always means away from the driver.
    private void driveFieldRelative(double forward, double right, double rotate) {
        double theta = Math.atan2(forward, right);   // stick angle
        double r = Math.hypot(right, forward);       // stick strength
        theta = AngleUnit.normalizeRadians(theta -
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        drive(r * Math.sin(theta), r * Math.cos(theta), rotate);
    }

    // Mecanum math: combine forward, strafe and turn into 4 wheel powers.
    public void drive(double forward, double right, double rotate) {
        double flP = forward + right + rotate;
        double frP = forward - right - rotate;
        double brP = forward + right - rotate;
        double blP = forward - right + rotate;

        // If any wheel is over 1.0, scale all down together so the direction stays correct.
        double max = Math.max(1.0, Math.max(Math.max(Math.abs(flP), Math.abs(frP)),
                Math.max(Math.abs(brP), Math.abs(blP))));

        fL.setPower(maxSpeed * flP / max);
        fR.setPower(maxSpeed * frP / max);
        bL.setPower(maxSpeed * blP / max);
        bR.setPower(maxSpeed * brP / max);
    }
}