package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    public DcMotorEx intakeMain;
    public CRServo fLIntake;
    public CRServo fRIntake;

    public Intake(HardwareMap hardwareMap){
        intakeMain = hardwareMap.get(DcMotorEx.class, "intake");
        fRIntake = hardwareMap.get(CRServo.class, "fRIntake");
        fLIntake = hardwareMap.get(CRServo.class, "fLIntake");

        intakeMain.setDirection(DcMotor.Direction.FORWARD);
        fLIntake.setDirection(DcMotorSimple.Direction.FORWARD);

        intakeMain.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        intakeMain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void intakeOn(double intakePower){
        intakeMain.setPower(intakePower);
    }
    public void intakeOff(){
        intakeMain.setPower(0);
    }
    public void intakeServosOn(double intakeServosPower){
        fRIntake.setPower(intakeServosPower);
        fLIntake.setPower(-1 * intakeServosPower);
    }

}
