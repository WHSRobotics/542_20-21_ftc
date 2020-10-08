package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

public class WHSTeleOp extends OpMode {
     WHSRobotImpl robot;

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
        if (gamepad2.x) {
            robot.canister.operateCanister(gamepad2.x);
        }
        else {
            robot.canister.operateCanister(false);
        }
        //Drivetrain
        robot.estimateHeading();
        robot.drivetrain.switchFieldCentric(gamepad1.b);
        if (gamepad1.left_bumper){
            robot.drivetrain.operateMecanumDrive(gamepad1.left_stick_x/2.54, gamepad1.left_stick_y/2.54, gamepad1.right_stick_x/2.54, robot.getCoordinate().getHeading());
        }else{
            robot.drivetrain.operateMecanumDriveScaled(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, robot.getCoordinate().getHeading());
        }
        //Outtake Launcher
        if (gamepad2.x){
            robot.outtake.operateTargetLaunch(gamepad2.x, gamepad2.y);
        }
        else {
            robot.outtake.operateTargetLaunch(false, false);
        }
        //Outtake Angle
        if (gamepad2.left_bumper){
            robot.outtake.operateLaunchAngle(gamepad2.left_bumper, gamepad2.right_bumper);
        }
        else if (gamepad2.right_bumper){
            robot.outtake.operateLaunchAngle(gamepad2.left_bumper, gamepad2.right_bumper);
        }

        //Wobble Claw
        if (gamepad2.a) {
            robot.wobble.operateClaw(gamepad2.a);
        }
        else {
            robot.wobble.operateClaw(false);
        }
        //Wobble Arm
        if (gamepad2.dpad_down) {
            robot.wobble.operateArm(gamepad2.dpad_down); //inside the robot at start of the match
        }
        else if (gamepad2.dpad_up) {
            robot.wobble.operateArm(gamepad2.dpad_up); //raise arm above the boundry
        }
        else if (gamepad2.dpad_right) {
            robot.wobble.operateArm(gamepad2.dpad_right); //raise arm up
        }
        else if (gamepad2.dpad_left) {
            robot.wobble.operateArm(gamepad2.dpad_left); //raise arm down
        }
        telemetry.addData("Outtake Motor State",robot.outtake.launchStateDescription);
        telemetry.addData("Outtake Angle State", robot.outtake.angleStateDescription);
        telemetry.addData("Canister State", robot.canister.canisterState);
        telemetry.addData("Intake State", robot.intake.intakeStateDescription);
        telemetry.addData("Wobble Arm State", robot.wobble.armStateDescription);
        telemetry.addData("Wobble Claw State", robot.wobble.clawStateDescription);
    }
}
