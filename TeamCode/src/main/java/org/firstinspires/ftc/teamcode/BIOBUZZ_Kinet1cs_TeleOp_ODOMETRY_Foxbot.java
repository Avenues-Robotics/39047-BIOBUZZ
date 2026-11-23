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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.logger.java.Configurable;
import org.firstinspires.ftc.teamcode.logger.java.LoggerTelemetry;

/*
 * This OpMode illustrates how to program your robot to drive field relative.  This means
 * that the robot drives the direction you push the joystick regardless of the current orientation
 * of the robot.
 *
 * This OpMode assumes that you have four mecanum wheels each on its own motor named:
 *   front_left_motor, front_right_motor, back_left_motor, back_right_motor
 *
 *   and that the left motors are flipped such that when they turn clockwise the wheel moves backwards
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 *
 */

// we need to configure odom, find hive cords

@Configurable
@TeleOp(name = "Foxbot Kinet1cs WIP With Odometry", group = "Robot")
public class BIOBUZZ_Kinet1cs_TeleOp_ODOMETRY_Foxbot extends OpMode {
    // declaration of motors
    private Drive drive;
    private Transfer transfer;
    private Intake intake;
    private Outtake outtake;
    private Odometry odometry;

    private LoggerTelemetry telemetry;

    //DistanceSensor distanceRight; //relative to front of robot these might be wrong
    //DistanceSensor distanceLeft; //relative to front of robot AKA intake





    // VARIABLES
    public static double intakePower = 0.5;
    public static double outtakeVelocity = 1320;
    public static double transferPower = 0.2;
    public static double intakeServosPower = 0.3;
    public static double hiveOgX = 123; //remember that these are AIMING POINTS
    public static double hiveOgY = -127; //og means that the original hive position; aka the og hiveX is
    public static double hiveInX = 123; //the hiveX where it originally started up; In means inverted
    public static double hiveInY = -127; //remember to configure!!!
    public static double aimedHiveX = 0;
    public static double aimedHiveY = 0; //change these
    public static double rotationSpeed;
    public boolean odometryConfigured = true;
    public double turningTo = 0;
    public static double outtakeVelocityDO = 1300;
    public static double outtakeVelocityCorner = 1320;

    @Override
    public void init() {
        drive = new Drive(hardwareMap);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);
        telemetry = new LoggerTelemetry(this);
        odometry = new Odometry(hardwareMap);
        telemetry.addLine("ALL SYSTEMS GO GO GO");
    }

    @Override
    public void loop() {
        outtake.setPIDFCoefficients();
        if (odometryConfigured){
            odometry.configureOdometryPos();
            odometryConfigured = false;
        }
        if (odometry.getY() > 0){
            aimedHiveX = hiveOgX;
            aimedHiveY=hiveOgY;
        } else {
            aimedHiveX = hiveInX;
            aimedHiveY = hiveInY;
        }
        if (gamepad1.right_bumper) {
            pointAndShoot();
            if (Math.abs(Drive.error) < 3){
                transfer.transferOn(transferPower);
            }
        } else if (gamepad1.left_bumper){
            transfer.transferOff();
        }

        //this will trigger the intake. when it runs we want the outtake stopped and when it's not
        //running we want it running.
        if (gamepad1.right_trigger > 0.5) {
            intake.intakeOn(intakePower);
            gamepad1.rumble(0.25, 0.25, 1);
        } else {
            intake.intakeOff();
        }
        intake.intakeServosOn(intakeServosPower);
        if (Drive.isTurning){
            drive.rotateToAngle(turningTo, 0.5, odometry.getHeading());
        } else {
            drive.driveFieldRelative(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }


        //TELEMETRY
        telemetry.addLine("julien and fuyuan write good code :)");
        telemetry.addLine("current outtake velocity:" + outtake.getVelocity());
        telemetry.addLine("current X: " + odometry.getX());
        telemetry.addLine("current Y: " + odometry.getY());
        telemetry.addLine("current heading: " + odometry.getHeading());
        telemetry.addGraph("heading:", odometry.getHeading());
        telemetry.addGraph("target heading: ", odometry.getHiveBearingDegrees(odometry.getX(), odometry.getY(), aimedHiveX, aimedHiveY));
        telemetry.addLine("distance to hive (CM): " + odometry.getHiveDistance(odometry.getX(), odometry.getY(), aimedHiveX, aimedHiveY));
        telemetry.update();
    }

    public void pointAndShoot(){
        drive.killMotors();
        Drive.isTurning = true;
        turningTo = (odometry.getHiveBearingDegrees(odometry.getX(), odometry.getY(), aimedHiveX, aimedHiveY) + 180);
        outtake.setVelocity(outtakeVelocity); //put regression determined equation here
    }

}



