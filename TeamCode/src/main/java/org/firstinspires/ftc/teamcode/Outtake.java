package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.bylazar.configurables.annotations.Configurable; // 🚨Fixed: the logger package is not in the repo, so this would not build

@Configurable
public class Outtake {
    DcMotorEx outtakeMain;
    public static double p = 32.0;
    public static double i = 0.1;
    public static double d = 0;
    public static double f = 13.05;

    public Outtake(HardwareMap hardwareMap){
        outtakeMain = hardwareMap.get(DcMotorEx.class, "outtake");

        outtakeMain.setDirection(DcMotor.Direction.FORWARD); //MAY NEED CHANGING

        outtakeMain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void outtakeOn(double outtakeVelocity){
        outtakeMain.setVelocity(outtakeVelocity);
    }
    public void outtakeOff(){
        outtakeMain.setVelocity(1);
    }
    public double getVelocity(){
        return outtakeMain.getVelocity();
    }
    public void setVelocity(double velocity){
        outtakeMain.setVelocity(velocity);
    }
    public void setPIDFCoefficients(){
        outtakeMain.setVelocityPIDFCoefficients(p, i, d, f);
    }

}
