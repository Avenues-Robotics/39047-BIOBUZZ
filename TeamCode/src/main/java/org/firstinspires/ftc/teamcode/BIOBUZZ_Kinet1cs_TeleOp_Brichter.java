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
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

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
@Configurable
@TeleOp(name = "Kinet1cs Bare Bones Teleop", group = "Robot")
public class BIOBUZZ_Kinet1cs_TeleOp_Brichter extends OpMode {
    // declaration of motors
    DcMotorEx fL;
    DcMotorEx fR;
    DcMotorEx bL;
    DcMotorEx bR;
    DcMotorEx outtake;
    CRServo transfer;
    private Intake intake;
    //DistanceSensor distanceRight; //relative to front of robot these might be wrong
    //DistanceSensor distanceLeft; //relative to front of robot AKA intake
    //GoBildaPinpointDriver pinpoint; //odometry stuff




    // This declares the IMU needed to get the current direction the robot is facing
    IMU imu;

    // VARIABLES
    public static double intakePower = 0.3;
    public static double outtakeVelocity = 2200;
    public static double transferPower = 0.4;
    public static double intakeServosPower = 0.3;
    public  String rightHiveCoordinates = "(0,0)"; //gotta change this

    @Override
    public void init() {
        //configures every motor to the hardware map on the driver's station
        fL = hardwareMap.get(DcMotorEx.class, "fL");
        fR = hardwareMap.get(DcMotorEx.class, "fR");
        bL = hardwareMap.get(DcMotorEx.class, "bL");
        bR = hardwareMap.get(DcMotorEx.class, "bR");
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");
        transfer = hardwareMap.get(CRServo.class, "transfer");


// certain motor need to be set in reverse in order for forward movement to work correctly
        bL.setDirection(DcMotor.Direction.FORWARD);
        fL.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.REVERSE);
        fL.setDirection(DcMotor.Direction.REVERSE);
        outtake.setDirection(DcMotor.Direction.FORWARD); //MAY NEED CHANGING

// This uses RUN_USING_ENCODER to be more accurate.   If you don't have the encoder
// wires, you should remove these
        fL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hardwareMap.get(IMU.class, "imu");
// The orientation is correct. Do not change this below.
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        /* ODOMETRY STUFF
        // Get a reference to the sensor
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        // Configure the sensor
        configurePinpoint();

        // Set the location of the robot - this should be the place you are starting the robot from
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
        */
    }

    @Override
    public void loop() {
        //this will trigger the transfer
        if (gamepad1.right_bumper && outtake.getVelocity() >= (0.95 * outtakeVelocity)) {
            letsOuttake();

        } else {
            transfer.setPower(0);
        }
        //drive function
        drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        //this will trigger the intake. when it runs we want the outtake stopped and when it's not
        //running we want it running.
        if (gamepad1.right_trigger > 0.5) {
            intake.intakeOn(intakePower);
        } else {
            intake.intakeOff();
            outtakeVelocity = 1500;
        }
        intake.intakeServosOn(intakeServosPower);

        outtake.setVelocity(outtakeVelocity);

        //TELEMETRY
        telemetry.addLine("julien and fuyuan write good code :)");
        if (outtakeVelocity - outtake.getVelocity() < 100 && outtakeVelocity - outtake.getVelocity() > -100){
            telemetry.addLine("outtake velocity set to:" + outtakeVelocity);
        }
        telemetry.addLine("current outtake velocity:" + outtake.getVelocity());

        //ODOMETRY
        /*
        pinpoint.update();
        Pose2D pose2D = pinpoint.getPosition();

        telemetry.addData("X coordinate (IN)", pose2D.getX(DistanceUnit.INCH));
        telemetry.addData("Y coordinate (IN)", pose2D.getY(DistanceUnit.INCH));
        telemetry.addData("Heading angle (DEGREES)", pose2D.getHeading(AngleUnit.DEGREES));

         */

    }
    public void letsOuttake(){ //function to activate the transfer
        transfer.setPower(transferPower);
    }

    public void drive(double forward, double right, double rotate) {
// This calculates the power needed for each wheel based on the amount of forward,
// strafe right, and rotate
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double maxPower = 1.0;
        double maxSpeed = 1.0;  // make this slower for outreaches

// This is needed to make sure we don't pass > 1.0 to any wheel
// It allows us to keep all of the motors in proportion to what they should
// be and not get clipped
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

// We multiply by maxSpeed so that it can be set lower for outreaches
// When a young child is driving the robot, we may not want to allow full
// speed.
        fL.setPower(maxSpeed * (frontLeftPower / maxPower));
        fR.setPower(maxSpeed * (frontRightPower / maxPower));
        bL.setPower(maxSpeed * (backLeftPower / maxPower));
        bR.setPower(maxSpeed * (backRightPower / maxPower));
    }
    //ODOMETRY

    //public void configurePinpoint(){
       /*
        *  Set the odometry pod positions relative to the point that you want the position to be measured from.
        *
        *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
        *  Left of the center is a positive number, right of center is a negative number.
        *
        *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
        *  Forward of center is a positive number, backwards is a negative number.
        */
      //  pinpoint.setOffsets(-84.0, -168.0, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1

    /*
     * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
     * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
     * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
     * number of ticks per unit of your odometry pod.  For example:
     *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
     */
     //   pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

    /*
     * Set the direction that each of the two odometry pods count. The X (forward) pod should
     * increase when you move the robot forward. And the Y (strafe) pod should increase when
     * you move the robot to the left.
     */
    //    pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
 //   GoBildaPinpointDriver.EncoderDirection.FORWARD);

    /*
     * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
     * The IMU will automatically calibrate when first powered on, but recalibrating before running
     * the robot is a good idea to ensure that the calibration is "good".
     * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
     * This is recommended before you run your autonomous, as a bad initial calibration can cause
     * an incorrect starting value for x, y, and heading.
     */
      //  pinpoint.resetPosAndIMU();
}


