package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Distance_Sensor {
    private DigitalChannel distanceSensorFrontRight;
    private DigitalChannel distanceSensorFrontLeft;
    private DigitalChannel distanceSensorOuttake;
    public static boolean lastRightDetected = false;
    public static boolean lastLeftDetected = false;
    public static boolean lastOuttakeDetected = false;
    public static int balls = 4;
    private int ballsCounted = 0;
    public Distance_Sensor(HardwareMap hardwareMap){
        distanceSensorFrontRight = hardwareMap.get(DigitalChannel.class, "distanceSensorFrontRight");
        distanceSensorFrontLeft = hardwareMap.get(DigitalChannel.class, "distanceSensorFrontLeft");
        distanceSensorOuttake = hardwareMap.get(DigitalChannel.class, "distanceSensorOuttake");

        distanceSensorFrontRight.setMode(DigitalChannel.Mode.INPUT);
        distanceSensorFrontLeft.setMode(DigitalChannel.Mode.INPUT);
        distanceSensorOuttake.setMode(DigitalChannel.Mode.INPUT);
    }

    public int getIntakeBalls(){
        ballsCounted = 0;
        if (distanceSensorFrontRight.getState() && !lastRightDetected){
            lastRightDetected = true;
            ballsCounted += 1;
        } else  if (!distanceSensorFrontRight.getState()){
            lastRightDetected = false;
        }
        if (distanceSensorFrontLeft.getState() && !lastLeftDetected){
            lastLeftDetected = true;
            ballsCounted += 1;
        } else if (!distanceSensorFrontLeft.getState()){
            lastLeftDetected = false;
        }
        return ballsCounted;
    }
    public int getOuttakeBalls(){
        if (distanceSensorOuttake.getState() && !lastOuttakeDetected){
            lastOuttakeDetected = true;
            return 1;
        } else {
            if (!distanceSensorOuttake.getState()){
                lastOuttakeDetected = false;
            }
        }
        return 0;
    }
    public int countBalls(){
        balls = balls + getIntakeBalls() - getOuttakeBalls();
        return balls;
    }

}
