package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// Autonomous path that mirror of Left: same path, turns right first.
// All the motor code lives in Drive.java, so this file only lists the moves.
@Autonomous(name = "AutoPushbotRight")
public class AutoPushbotRight extends LinearOpMode {

    @Override
    public void runOpMode() {

        // set up the motors through the shared Drive class ("this" = this OpMode)
        Drive robot = new Drive(this);

        // pause until driver starts. everything above runs during init
        waitForStart();

        // drive(speed, ticks) and rotate(speed, degrees)
        robot.drive(0.5, 3200);
        robot.rotate(0.5, 130);
        robot.drive(0.5, 1400);
        robot.rotate(0.5, -260);
        robot.drive(0.5, 3200);
    }
}
