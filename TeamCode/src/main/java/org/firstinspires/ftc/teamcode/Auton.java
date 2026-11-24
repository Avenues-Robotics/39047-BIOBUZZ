package org.firstinspires.ftc.teamcode;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Configurable
@Autonomous(name = "Autonomous", group = "Robot")
public class Auton extends LinearOpMode {
    public static double hiveX = 123;          //where to aim
    public static double hiveY = -127;         //where to aim
    public static double degreePower = 0.02;   //turn power per degree of error
    public static double minPower = 0.12;      //min turn power
    public static double maxPower = 0.5;       //max turn power
    public static double aimTolerance = 3;     //within this many degrees counts as aimed
    public static double shootVelocity = 1320; //flywheel speed 🚨🚨🚨: estimate
    public static double velTolerance = 60;    //within this much of shootVelocity counts as ready 🚨🚨🚨: estimate
    public static double transferPower = 0.2;  //power of the transfer
    public static double intakePower = 0.5;    //power of the intake
    public static double shootSeconds = 4;     //how long to feed balls 🚨🚨🚨: estimate
    public static double leavePower = 0.4;     //drive power for leaving
    public static double leaveSeconds = 1;     //how long to drive when leaving 🚨🚨🚨: estimate

    private Drive drive;
    private Transfer transfer;
    private Intake intake;
    private Outtake outtake;
    private Odometry odometry;

    @Override
    public void runOpMode() {
        drive = new Drive(hardwareMap);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);
        odometry = new Odometry(hardwareMap);

        telemetry.addLine("Autonomous ready");
        telemetry.update();

        waitForStart();

        odometry.configureOdometryPos();
        outtake.setPIDFCoefficients();
        outtake.setVelocity(shootVelocity);

        //Aiming Code
        double startTime = getRuntime();
        while (opModeIsActive() && getRuntime() - startTime < 3) { //give up after 3 seconds
            double err = getAimError();
            if (Math.abs(err) < aimTolerance) {
                break;
            }

            double power = degreePower * Math.abs(err);
            if (power < minPower) {
                power = minPower;
            }
            if (power > maxPower) {
                power = maxPower;
            }

            if (err > 0) {
                drive.drive(0, 0, -power);
            } else {
                drive.drive(0, 0, power);
            }

            telemetry.addLine("Aiming...");
            telemetry.addLine("err: " + err);
            telemetry.update();
        }
        drive.killMotors();

        //Velocity Loading Code
        startTime = getRuntime();
        while (opModeIsActive() && getRuntime() - startTime < 2) {
            if (Math.abs(outtake.getVelocity() - shootVelocity) < velTolerance) {
                break;
            }
            telemetry.addLine("Velocity Loading...");
            telemetry.addLine("outtake velocity: " + outtake.getVelocity());
            telemetry.update();
        }

        //Shooting Code
        transfer.transferOn(transferPower);
        intake.intakeOn(intakePower);
        startTime = getRuntime();
        while (opModeIsActive() && getRuntime() - startTime < shootSeconds) {
            telemetry.addLine("Shooting...");
            telemetry.addLine("outtake velocity: " + outtake.getVelocity());
            telemetry.update();
        }
        transfer.transferOff();
        intake.intakeOff();
        outtake.setVelocity(0);

        //Leaving Code
        startTime = getRuntime();
        while (opModeIsActive() && getRuntime() - startTime < leaveSeconds) {
            drive.drive(leavePower, 0, 0);
            telemetry.addLine("Leaving");
            telemetry.update();
        }
        drive.killMotors();

        telemetry.addLine("Autonomous Completed");
        telemetry.update();
    }


    public double getAimError() {
        double robotX = odometry.getX();
        double robotY = odometry.getY();
        double robotHeading = odometry.getHeading();

        double targetHeading = Math.toDegrees(Math.atan2(hiveY - robotY, hiveX - robotX)); //shown in desmos

        double err1 = (targetHeading - robotHeading + 180) % 360; //E1
        if (err1 < 0) {
            err1 = err1 + 360;
        }

        double err = err1;//E
        if (err1 > 180) {
            err = err1 - 360;
        }
        return err;
    }
}
