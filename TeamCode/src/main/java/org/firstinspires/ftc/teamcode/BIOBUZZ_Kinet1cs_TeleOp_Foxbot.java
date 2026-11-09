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
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
@TeleOp(name = "Foxbot Kinet1cs Bare Bones Teleop", group = "Robot")
public class BIOBUZZ_Kinet1cs_TeleOp_Foxbot extends OpMode {
    // declaration of motors
    private Drive drive;
    private Transfer transfer;
    private Intake intake;
    private Outtake outtake;
    private Odometry odometry;

    //DistanceSensor distanceRight; //relative to front of robot these might be wrong
    //DistanceSensor distanceLeft; //relative to front of robot AKA intake





    // VARIABLES
    public static double intakePower = 0.3;
    public static double outtakeVelocity = 2200;
    public static double transferPower = 0.4;
    public static double intakeServosPower = 0.3;
    public  String rightHiveCoordinates = "(0,0)"; //gotta change this

    @Override
    public void init() {
        telemetry.addLine("ALL SYSTEMS GO GO GO");
    }

    @Override
    public void loop() {
        //this will trigger the transfer
        if (gamepad1.right_bumper) {
            transfer.transferOn(transferPower);
        } else {
            transfer.transferOff();
        }
        //drive function
        drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        //this will trigger the intake. when it runs we want the outtake stopped and when it's not
        //running we want it running.
        if (gamepad1.right_trigger > 0.5) {
            intake.intakeOn(intakePower);
            gamepad1.rumble(0.25, 0.25, 1);
        } else {
            intake.intakeOff();
            outtakeVelocity = 1500;
        }
        intake.intakeServosOn(intakeServosPower);

        outtake.outtakeOn(outtakeVelocity);

        //TELEMETRY
        telemetry.addLine("julien and fuyuan write good code :)");
        telemetry.addLine("current outtake velocity:" + outtake.getVelocity());
    }

    public void pointAndShoot(double relativeAngle, double rotationSpeed){
        drive.killMotors();
        drive.rotate(relativeAngle, rotationSpeed);
        outtake.setVelocity(1); //put regression determined equation here
    }

}



