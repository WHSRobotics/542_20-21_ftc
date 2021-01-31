package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotJank;

@TeleOp(name = "Jank telly tubby")
public class WHSTeleJank extends OpMode {
    WHSRobotJank robot;
    @Override
    public void init() {
        robot = new WHSRobotJank(hardwareMap);
    }

    @Override
    public void loop() {
        robot.estimateHeading();
        robot.estimatePosition();
        robot.drivetrain.switchFieldCentric(gamepad1.b);

        if (gamepad1.left_bumper) {
            robot.drivetrain.operate(-gamepad1.left_stick_y / 5, -gamepad1.right_stick_y / 5);
        } else {
            robot.drivetrain.operate(-gamepad1.left_stick_y/2.54 , -gamepad1.right_stick_y/2.54);
        }

        robot.wobble.jankOperate(gamepad1.right_bumper,gamepad1.left_bumper, gamepad1.y);

    }
}
