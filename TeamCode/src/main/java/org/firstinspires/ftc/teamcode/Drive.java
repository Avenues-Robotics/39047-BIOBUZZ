package org.firstinspires.ftc.teamcode;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Configurable
public class Drive {
    DcMotorEx fL;
    DcMotorEx fR;
    DcMotorEx bL;
    DcMotorEx bR;
    IMU imu;
    public static double offset = 100;
    public static boolean isTurning = false;
    public static double error;
    public Drive(HardwareMap hardwareMap){
        fL = hardwareMap.get(DcMotorEx.class, "fL");
        fR = hardwareMap.get(DcMotorEx.class, "fR");
        bL = hardwareMap.get(DcMotorEx.class, "bL");
        bR = hardwareMap.get(DcMotorEx.class, "bR");

        fL.setDirection(DcMotor.Direction.REVERSE);
        fR.setDirection(DcMotor.Direction.FORWARD);
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

        imu = hardwareMap.get(IMU.class, "imu");
        // This needs to be changed to match the orientation on your robot
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        imu.resetYaw(); // 🚨Fixed: field relative used a heading left over from the last run. Face the robot away from the driver at init
        isTurning = false; // 🚨Fixed: it is static, so a turn left over from the last OpMode locked the sticks
    }
    void driveFieldRelative(double forward, double right, double rotate) {
        // First, convert direction being asked to drive to polar coordinates
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);

        // Second, rotate angle by the angle the robot is pointing
        theta = AngleUnit.normalizeRadians(theta -
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Third, convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // Finally, call the drive method with robot relative forward and right amounts
        drive(newForward, newRight, rotate);
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

    public void rotateToAngle(double degrees, double speed, double robotAngle){ //90º = start pos
        error = degrees - robotAngle;
        while (error > 180) error -= 360; // 🚨Fixed: goes the short way around. Crossing 180 gave errors like 340 and the robot did nothing
        while (error < -180) error += 360; // 🚨Fixed: same, for the other side

        if (Math.abs(error) > 3){ // 🚨Fixed: was "error < 180" with no abs. Not needed now that the error is wrapped
            double turnPower = Math.max(0.12, Math.min(speed, 0.02 * Math.abs(error))); // 🚨Fixed: no cap gave full power spins, and tiny power near the target got stuck. "speed" was never used
            if (error < 0) turnPower = -turnPower; // 🚨Fixed: keeps the turn direction after the cap
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
