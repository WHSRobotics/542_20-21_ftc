package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

public class WHSTeleOp extends OpMode {
     WHSRobotImpl robot;
    public Toggler targetTog = new Toggler(6);
    public Position currentTarget;

    @Override
    public void init()  {
        robot = new WHSRobotImpl(hardwareMap);
    }

    @Override
    public void loop(){
    //Intake
        if (gamepad1.right_trigger >0.01) {
            robot.intake.operate(gamepad1.right_trigger > 0.01, gamepad1.left_trigger == 0.0);
        }
        else if (gamepad1.left_trigger >0.01) {
            robot.intake.operate(gamepad1.right_trigger > 0.01, gamepad1.left_trigger > 0.01);
        }
        else {
            robot.intake.operate(gamepad1.right_trigger == 0, gamepad1.left_trigger == 0);
        }

        //Canister
        if (gamepad2.x || gamepad2.y) {
            robot.canister.operateCanister(gamepad2.x, gamepad2.y);
        }
        else {
            robot.canister.operateCanister(false, false);
        }
        //Drivetrain
        robot.estimateHeading();
        robot.drivetrain.switchFieldCentric(gamepad1.b);
        if (gamepad1.left_bumper){
            robot.drivetrain.operateMecanumDrive(gamepad1.left_stick_x/2.54, gamepad1.left_stick_y/2.54, gamepad1.right_stick_x/2.54, robot.getCoordinate().getHeading());
        }else{
            robot.drivetrain.operateMecanumDriveScaled(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, robot.getCoordinate().getHeading());
        }
        //Outtake
        /*if (gamepad2.dpad_up) {
            if (gamepad2.dpad_left)
            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(robot.outtake.powershot1, robot.getCoordinate()), false);
            robot.outtake.flap.setPosition(robot.outtake.calculateLaunchSetting(robot.outtake.calculateDistanceToTarget(robot.outtake.powershot1, robot.getCoordinate()), robot.outtake.POWER_SHOT_TARGET_HEIGHT));
        }*/
        //Wobble
        if(gamepad1.dpad_up){
            robot.wobble.operateArm(gamepad1.dpad_up);
        }
        if (gamepad1.dpad_down){
            robot.wobble.operateClaw(gamepad1.dpad_down);
        }
    }
}
