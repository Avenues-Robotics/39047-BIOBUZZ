package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.bylazar.configurables.annotations.Configurable; // 🚨Fixed: the logger package is not in the repo, so this would not build
//import org.firstinspires.ftc.teamcode.logger.java.LoggerTelemetry; // 🚨Fixed: not in the repo
@Configurable
@TeleOp(name = "PIDF Testing", group = "Robot")
public class PIDF_Testing extends OpMode {
    DcMotorEx outtakeMain;
    //private LoggerTelemetry telemetry; // 🚨Fixed: uses the normal telemetry instead

    double targetVelocity = 1500;
    double maxV = 2520;
    public static double f = 13;
    public static double p = 140;
    public static double i = 0;
    public static double d = 0;

    public void init(){
        outtakeMain = hardwareMap.get(DcMotorEx.class, "outtake");

        outtakeMain.setDirection(DcMotor.Direction.FORWARD); //MAY NEED CHANGING

        outtakeMain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //telemetry = new LoggerTelemetry(this); // 🚨Fixed: not in the repo

        outtakeMain.setVelocityPIDFCoefficients(p, i, d, f);
    }

    public void loop(){
        if (gamepad1.a){
            outtakeMain.setVelocity(targetVelocity);
        }
        if (gamepad1.b){
            outtakeMain.setVelocity(0);
        }

        telemetry.update();
        telemetry.addData("outtake vel", outtakeMain.getVelocity()); // 🚨Fixed: addGraph only exists in the missing logger
        telemetry.addData("outtake target", targetVelocity); // 🚨Fixed: addGraph only exists in the missing logger
        telemetry.addLine("current outtake velocity:" + outtakeMain.getVelocity());
    }

}
