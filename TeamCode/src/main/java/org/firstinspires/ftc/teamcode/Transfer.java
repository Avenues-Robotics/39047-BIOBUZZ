package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class Transfer {
    DcMotorEx transferMain;
    public Transfer(HardwareMap hardwareMap){
        transferMain = hardwareMap.get(DcMotorEx.class, "transfer");

        transferMain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        transferMain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void transferOn(double transferPower){
        transferMain.setPower(transferPower);
    }
    public void transferOff(){
        transferMain.setPower(0);
    }
    public boolean isTransferOn(){
        if (transferMain.getPower() > 0){
            return true;
        } else {
            return false;
        }
    }

}
