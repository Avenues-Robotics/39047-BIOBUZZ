package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Drive {

    // the two drive motors, shared by every method in this class
    private DcMotor motorLeft;
    private DcMotor motorRight;

    // the OpMode that is running, so we can use opModeIsActive() and telemetry
    private LinearOpMode opMode;

    // Constructor: runs once when an OpMode does "new Drive(this)"
    public Drive(LinearOpMode opMode) {
        this.opMode = opMode;

        // Create two DcMotor OBJECTS connected to the motors on
        // the robot named motorLeft and motorRight
        motorLeft = opMode.hardwareMap.get(DcMotor.class, "motorLeft");
        motorRight = opMode.hardwareMap.get(DcMotor.class, "motorRight");

        // reverse the left wheel so both wheels move forward in the same direction.
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.FORWARD);

        // turn on encoders so the motor counts ticks and holds a steady speed
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void drive(double speed, int ticks) {

        // only running if op mode is on and also is not stopped by driver
        if (opMode.opModeIsActive()) {

            // Reset both encoder counts to zero so the target is measured from current position
            motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // both motors go the same distance so the robot drives straight
            motorLeft.setTargetPosition(ticks);
            motorRight.setTargetPosition(ticks);

            // Each motor drives to set position and stops.
            motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start the motors moving
            motorLeft.setPower(speed);
            motorRight.setPower(speed);

            // loop until either motor reaches position or opmode stops running
            while (opMode.opModeIsActive() &&
                   (motorLeft.isBusy() && motorRight.isBusy())) {

                // Show target and live position on driver station while driving
                opMode.telemetry.addData("Driving to", ticks);
                opMode.telemetry.addData("Current pos", "Left: " + motorLeft.getCurrentPosition() + " | Right: " + motorRight.getCurrentPosition());
                opMode.telemetry.update();
            }

            // show final encoder position to check how close each motor got
            opMode.telemetry.addData("Driving to", ticks);
            opMode.telemetry.addData("Current pos", "Left: " + motorLeft.getCurrentPosition() + " | Right: " + motorRight.getCurrentPosition());
            opMode.telemetry.update();

            // Cut power to both motors
            motorLeft.setPower(0);
            motorRight.setPower(0);
        }
    }

    public void rotate(double speed, double degrees) {

        // only running if op mode is on and not stopped by driver
        if (opMode.opModeIsActive()) {

            // convert degrees to encoder ticks
            int ticks = (int) (degrees * 11.1);

            // Reset both encoder counts to zero so the target is measured from current position
            motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // positive degrees = right turn, negative degrees = left turn
            motorLeft.setTargetPosition(ticks);
            motorRight.setTargetPosition(-ticks);

            // Each motor drives to set position and stops.
            motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start the motors moving
            motorLeft.setPower(speed);
            motorRight.setPower(speed);

            // loop until either motor reaches position or opmode stops running
            while (opMode.opModeIsActive() &&
                   (motorLeft.isBusy() && motorRight.isBusy())) {

                // Show target and live position on driver station while turning
                opMode.telemetry.addData("Rotating", degrees + " deg (" + ticks + " ticks)");
                opMode.telemetry.addData("Current pos", "Left: " + motorLeft.getCurrentPosition() + " | Right: " + motorRight.getCurrentPosition());
                opMode.telemetry.update();
            }

            // show final encoder position to check how close each motor got
            opMode.telemetry.addData("Rotating", degrees + " deg (" + ticks + " ticks)");
            opMode.telemetry.addData("Current pos", "Left: " + motorLeft.getCurrentPosition() + " | Right: " + motorRight.getCurrentPosition());
            opMode.telemetry.update();

            // Cut power to both motors
            motorLeft.setPower(0);
            motorRight.setPower(0);
        }
    }
}
