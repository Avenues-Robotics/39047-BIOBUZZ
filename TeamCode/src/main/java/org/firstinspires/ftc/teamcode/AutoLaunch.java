package org.firstinspires.ftc.teamcode;
import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class AutoLaunch {
    public static double degreePower = 0.2; //power per degree, the amount of power to add or subtract per degree of error
    public static double minPower = 0.12; //minimum power to apply to the motors, this is to prevent the robot from stalling when the error is small
    public static double maxPower = 0.6; //maximum power to apply to the motors
    public static double transferPower = 0.2; //power to apply when transferring power between motors
    public static double aimTolerance = 5; //tolerance in degrees for aiming, the robot will stop aiming when the error is within this range
    public static double velBase = 2200; //base velocity for the outtake, this is the velocity that the outtake will try to maintain 🚨🚨🚨: 2200 is estimate, need to be tested and replaced
    public static double velSlope = 10; //slope for the velocity, this is the amount of velocity to add or subtract per degree of error 🚨🚨🚨: 10 is estimate, need to be tested and replaced
    public static double velTolerance = 50; //tolerance in velocity for the outtake, the outtake will stop adjusting when the error is within this range 🚨🚨🚨: 50 is estimate, need to be tested and replaced

    private final Drive drive;
    private final Outtake outtake;
    private final Transfer transfer;
    private final Odometry odometry;

    public double distance, headingError, targetVelocity;

    //constructor
    public AutoLaunch(Drive drive, Outtake outtake, Transfer transfer, Odometry odometry) {
        this.drive = drive;
        this.outtake = outtake;
        this.transfer = transfer;
        this.odometry = odometry;
    }

    //recalls this in the loop, returns True while feeding
    public boolean update(double hiveX, double hiveY) {
        double x = odometry.getX();
        double y = odometry.getY();

        distance = odometry.getDistanceTo(x,y,hiveX, hiveY); // distance to hive
        degreeError = odometry.getHiveAngleRelativeToBotDegrees(x,y, hiveX,hiveY) - odometry.getHeading(); // robot heading degree error relative to hive
        



    }

}
