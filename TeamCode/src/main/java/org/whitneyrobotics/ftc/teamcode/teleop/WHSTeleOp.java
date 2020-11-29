package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

public class WHSTeleOp extends OpMode {
     WHSRobotImpl robot;
    public Toggler targetTog = new Toggler(4);
    public Toggler binToggler = new Toggler(3);
    public Outtake.GoalPositions currentTarget;
    public Position currentTargetPos;
    public SimpleTimer launchTimer = new SimpleTimer();
    public final int LAUNCH_TIME = 500;

    public String  currentTargetWord;
    public String currentBinWord;
    public String intakeStatus;

    public final Position powershot1 = new Position(1800,-95.25); // from right to left fix later
    public final Position powershot2 = new Position(1800,-285.75);
    public final Position powershot3 = new Position(1800,-476.25);
    public final Position binsMidpoint = new Position(1800,-890.5875);


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
            currentTargetWord = "Left Powershot";
            currentTarget = Outtake.GoalPositions.LEFT_POWER_SHOT;
            currentTargetPos = powershot1;
        }
        else if (targetTog.currentState()==1){
            currentTargetWord = "Center Powershot";
            currentTarget = Outtake.GoalPositions.CENTER_POWER_SHOT;
            currentTargetPos = powershot2;
        }
        else if (targetTog.currentState()==2){
            currentTargetWord = "Right Powershot";
            currentTarget = Outtake.GoalPositions.RIGHT_POWER_SHOT;
            currentTargetPos = powershot3;

        }
        else{
            currentTargetWord = "Bins";
            currentTargetPos = binsMidpoint;
            if(binToggler.currentState()==0){
                currentTarget = Outtake.GoalPositions.LOW_BIN;
                currentBinWord = "Low";
            }
            else if (binToggler.currentState()==1){
                currentTarget = Outtake.GoalPositions.MEDIUM_BIN;
                currentBinWord = "Medium";
            }
            else {
                currentTarget = Outtake.GoalPositions.HIGH_BIN;
                currentBinWord = "High";
            }
        }
        if (gamepad2.a){
            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(currentTargetPos, robot.getCoordinate()), false);
            launchTimer.set(LAUNCH_TIME);
            while (!launchTimer.isExpired()) {
                robot.outtake.operate(currentTarget);
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
