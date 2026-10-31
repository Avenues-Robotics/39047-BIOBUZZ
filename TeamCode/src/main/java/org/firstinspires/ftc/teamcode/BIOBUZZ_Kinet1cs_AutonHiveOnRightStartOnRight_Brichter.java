package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


// Note MyFIRSTJavaOpMode is the default OpMode class that FTCSIM runs
// Don't change the name of the class.
public class BIOBUZZ_Kinet1cs_AutonHiveOnRightStartOnRight_Brichter extends LinearOpMode {

    // declaring the motors as variables?
    DcMotorEx fL;
    DcMotorEx fR;
    DcMotorEx bL;
    DcMotorEx bR;
    DcMotorEx intakeMain;
    DcMotorEx outtake;
    CRServo transfer;
    CRServo fRIntake;
    CRServo fLIntake;

    @Override
    public void runOpMode() {

        // Create two DcMotor OBJECTS connected to the motors on
        // the robot named bL and bR
        fL = hardwareMap.get(DcMotorEx.class, "fL");
        fR = hardwareMap.get(DcMotorEx.class, "fR");
        bL = hardwareMap.get(DcMotorEx.class, "bL");
        bR = hardwareMap.get(DcMotorEx.class, "bR");
        intakeMain = hardwareMap.get(DcMotorEx.class, "intake");
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");
        transfer = hardwareMap.get(CRServo.class, "transfer");
        fRIntake = hardwareMap.get(CRServo.class, "fRIntake");
        fLIntake = hardwareMap.get(CRServo.class, "fLIntake");

        // Sets direction for the motors to move.
        bL.setDirection(DcMotor.Direction.FORWARD);
        fL.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.REVERSE);
        fL.setDirection(DcMotor.Direction.REVERSE);
        intakeMain.setDirection(DcMotor.Direction.FORWARD); //MAY NEED CHANGING
        outtake.setDirection(DcMotor.Direction.FORWARD); //MAY NEED CHANGING
        fLIntake.setDirection(DcMotorSimple.Direction.FORWARD);

        // Turns motors into an on state to be modified
        fL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Ensures that nothing runs without hte robot being started
        waitForStart();

        // Play around with the PARAMETERS inside the parentheses
        // Moves the robot in a specific directino
        drive(0.2, 3200);



    }

    public void drive(double speed, int ticks){

        // makes this only run if the robot is in run mode
        if (opModeIsActive()) {

            // resets sensors to ensure they don't mess up
            bL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // sets targets for where motors will move
            bL.setTargetPosition(ticks);
            bR.setTargetPosition(ticks);
            fL.setTargetPosition(ticks);
            fR.setTargetPosition(ticks);

            // runs the motors and makes them start moving
            bL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // sets speed of motors
            bL.setPower(speed);
            bR.setPower(speed);
            fL.setPower(speed);
            fR.setPower(speed);


            // sets power of motors after they are done running
            bL.setPower(0);
            bR.setPower(0);
            fL.setPower(0);
            fR.setPower(0);

            // displays current position to user
        }
    }
    public void rotate(double speed, double degrees){

        // makes this only run if the robot is in run mode
        if (opModeIsActive()) {
            int ticks = (int) (degrees * 16.5);

            // resets sensors to ensure they don't mess up
            bL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // sets targets for where motors will move
            if (degrees > 0) {
                bL.setTargetPosition(ticks);
                bR.setTargetPosition(-1 * ticks);
                fL.setTargetPosition(ticks);
                fR.setTargetPosition(-1 * ticks);
            } else {
                bL.setTargetPosition(ticks);
                bR.setTargetPosition(-1 *ticks);
                fL.setTargetPosition(ticks);
                fR.setTargetPosition(-1 *ticks);
            }
            // runs the motors and makes them start moving
            bL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // sets speed of motors
            bL.setPower(speed);
            bR.setPower(speed);
            fL.setPower(speed);
            fR.setPower(speed);

            bL.setPower(0);
            bR.setPower(0);
            fL.setPower(speed);
            fR.setPower(speed);
        }
    }

}
