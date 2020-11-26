package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

public class WHSTeleOp extends OpMode {
     WHSRobotImpl robot;
    public Toggler targetTog = new Toggler(4);
    public Toggler binToggler = new Toggler(2);
    public Position currentTarget;
    public double shootingHeight;
    public SimpleTimer launchTimer = new SimpleTimer();
    public final int LAUNCH_TIME = 500;

    public String  currentTargetWord;
    public String currentBinWord;
    public String intakeStatus;




    @Override
    public void init()  {
        robot = new WHSRobotImpl(hardwareMap);
    }

    @Override
    public void loop(){
        //Intake
        if (gamepad1.right_trigger >0.01) {
            intakeStatus = "Normal Intake";
            robot.intake.operate(gamepad1.right_trigger > 0.01, gamepad1.left_trigger == 0.0);
        }
        else if (gamepad1.left_trigger >0.01) {
            intakeStatus = "Reverse Intake";
            robot.intake.operate(gamepad1.right_trigger > 0.01, gamepad1.left_trigger > 0.01);
        }
        else {
            intakeStatus = "Power Off";
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
        targetTog.changeState(gamepad2.dpad_up, gamepad2.dpad_down);
        binToggler.changeState(gamepad2.dpad_right, gamepad2.dpad_left);
        if (targetTog.currentState()==0){
            currentTargetWord = "Powershot 1";
            currentTarget = robot.outtake.powershot1;
            shootingHeight = robot.outtake.POWER_SHOT_TARGET_HEIGHT;
        }
        else if (targetTog.currentState()==1){
            currentTargetWord = "Powershot 2";
            currentTarget = robot.outtake.powershot2;
            shootingHeight = robot.outtake.POWER_SHOT_TARGET_HEIGHT;
        }
        else if (targetTog.currentState()==2){
            currentTargetWord = "Powershot 3";
            currentTarget = robot.outtake.powershot3;
            shootingHeight = robot.outtake.POWER_SHOT_TARGET_HEIGHT;

        }
        else{
            currentTargetWord = "Bins";
            currentTarget = robot.outtake.binsMidpoint;
            if(binToggler.currentState()==0){
                currentBinWord = "Medium";
                shootingHeight = robot.outtake.MID_GOAL_HEIGHT;
            }
            else{
                currentBinWord = "High";
                shootingHeight = robot.outtake.HIGH_GOAL_HEIGHT;

            }
        }
        if (gamepad2.a){
            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(currentTarget, robot.getCoordinate()),false);
            robot.outtake.flap.setPosition(robot.outtake.calculateLaunchSetting(robot.outtake.calculateDistanceToTarget(currentTarget, robot.getCoordinate()), shootingHeight));
            launchTimer.set(LAUNCH_TIME);
            while(!launchTimer.isExpired()){
                robot.outtake.setLauncherPower(robot.outtake.FLYWHEEL_POWER);
            }
            robot.outtake.setLauncherPower(0);
        }
        //Wobble
        if(gamepad1.dpad_up){
            robot.wobble.operateArm(gamepad1.dpad_up);
        }
        if (gamepad1.dpad_down){
            robot.wobble.operateClaw(gamepad1.dpad_down);
        }
        telemetry.addData("Intake State: ", intakeStatus);
        telemetry.addData("Canister Loader State: ", robot.canister.canisterState);
        telemetry.addData("Canister Platform Orientation: ", robot.canister.platformState);
        telemetry.addData("Robot Current Position: ", robot.drivetrain.getAllEncoderPositions());
        telemetry.addData("Current Target: ", currentTargetWord);
        telemetry.addData("Current Bin(if Current Target is at Bins): ", currentBinWord);
        telemetry.addData("Wobble Arm State: ", robot.wobble.armStateDescription);
        telemetry.addData("Wobble Claw State: ", robot.wobble.clawStateDescription);
    }
}
