package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.logger.java.Configurable;
import org.firstinspires.ftc.teamcode.logger.java.LoggerTelemetry;
@Configurable
@TeleOp(name = "PIDF Testing", group = "Robot")
public class PIDF_Testing extends OpMode {
    DcMotorEx outtakeMain;
    private LoggerTelemetry telemetry;

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

        telemetry = new LoggerTelemetry(this);

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
        telemetry.addGraph("outtake vel", outtakeMain.getVelocity());
        telemetry.addGraph("outtake target", targetVelocity);
        telemetry.addLine("current outtake velocity:" + outtakeMain.getVelocity());
    }

}
