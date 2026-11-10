package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name = "AutoPushbotLeft")
public class AutoPushbotLeft extends LinearOpMode {

    @Override
    public void runOpMode() {

        // set up the motors through the shared Drive class
        Drive robot = new Drive(this);

        // pause until driver starts. everything above runs during init
        waitForStart();

        // drive(speed, ticks) and rotate(speed, degrees)
        robot.drive(0.5, 3200);
        robot.rotate(0.5, -130);
        robot.drive(0.5, 1400);
        robot.rotate(0.5, 260);
        robot.drive(0.5, 3200);
    }
}
