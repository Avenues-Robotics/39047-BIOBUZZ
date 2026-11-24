package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Configurable
public class Transfer {
    DcMotorEx transferMain;
    public static double ticksRound = 537.7; //5204 Series Yellow Jacket Planetary Gear Motor (19.2:1 Ratio, 80mm Length 8mm REX® Shaft, 312 RPM, 3.3 - 5V Encoder)
    public static double ticksTolerance = 5; //can be changed I JUST LIKE THE NUMBER 5
    private int formerTicks = 0; //bot tick
    public static double endingPower = 0.2;
    private boolean ending = false;

    public Transfer(HardwareMap hardwareMap){
        transferMain = hardwareMap.get(DcMotorEx.class, "transfer");

        transferMain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        transferMain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        transferMain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void transferOn(double transferPower){
        transferMain.setPower(transferPower);
    }

    public void stopAtRound(){
        ending = true;
    }

    public void transferUpdate(){
        if (ending){
            if (Math.abs(ticksError()) <= tolerance){
                transferMain.setPower(0);
                ending = false;
            } else {
                transferMain.setPower(finishPower);
            }
        }
    }

    public void transferOff(){
        if (transferMain.getPower() != 0){
            formerTicks = transferMain.getCurrentPosition();
        }
        transferMain.setPower(0);
    }

    public boolean isTransferOn(){
        ending = false;
        if (transferMain.getPower() > 0){
            return true;
        } else {
            return false;
        }
    }

    public int getTicks(){
        return transferMain.getCurrentPosition();
    }

    public int formerTicks(){
        return formerTicks;
    }

    public double ticksError(){
        double error = transferMain.getCurrentPosition() % ticksRound;
        if (error > ticksRound / 2){
            error = error - ticksRound;
        }
        return error;
    }
}
