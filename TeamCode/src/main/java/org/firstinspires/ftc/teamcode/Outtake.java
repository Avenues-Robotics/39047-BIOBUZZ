package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Outtake {
    DcMotorEx outtakeMain;

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

}
