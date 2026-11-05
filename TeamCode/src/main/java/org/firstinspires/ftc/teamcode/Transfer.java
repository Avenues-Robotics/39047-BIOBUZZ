package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class Transfer {
    public CRServo transferServo;
    public Transfer(HardwareMap hardwareMap){
        transferServo = hardwareMap.get(CRServo.class, "transfer");
    }
    public void transferOn(double transferPower){
        transferServo.setPower(transferPower);
    }
    public void transferOff(){
        transferServo.setPower(0);
    }
    public boolean isTransferOn(){
        if (transferServo.getPower() > 0){
            return true;
        } else {
            return false;
        }
    }

}
