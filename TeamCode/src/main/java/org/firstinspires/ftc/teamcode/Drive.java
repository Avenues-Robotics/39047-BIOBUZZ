package org.firstinspires.ftc.teamcode;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Configurable
public class Drive {
    DcMotorEx fL;
    DcMotorEx fR;
    DcMotorEx bL;
    DcMotorEx bR;
    public static double offset = 100;
    public static boolean isTurning = false;
    public Drive(HardwareMap hardwareMap){
        fL = hardwareMap.get(DcMotorEx.class, "fL");
        fR = hardwareMap.get(DcMotorEx.class, "fR");
        bL = hardwareMap.get(DcMotorEx.class, "bL");
        bR = hardwareMap.get(DcMotorEx.class, "bR");

        fL.setDirection(DcMotor.Direction.FORWARD);
        fR.setDirection(DcMotor.Direction.REVERSE);
        bL.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.REVERSE);

        fL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void mecanumDrive(double forward, double right, double rotate) {
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

    public void rotateToAngle(double degrees, double speed, double robotAngle){ //0º = start pos
        double error = Math.abs(robotAngle - degrees);

        if (error > 3){
            double turnPower = (0.02 * error);
            fL.setPower(-1 * turnPower);
            fR.setPower(turnPower);
            bL.setPower(-1 * turnPower);
            bR.setPower(turnPower);
        } else {
            isTurning = false;
            fL.setPower(0);
            fR.setPower(0);
            bL.setPower(0);
            bR.setPower(0);
        }
    }
    public void killMotors(){
        fL.setPower(0);
        bL.setPower(0);
        bR.setPower(0);
        fR.setPower(0);
    }

}
