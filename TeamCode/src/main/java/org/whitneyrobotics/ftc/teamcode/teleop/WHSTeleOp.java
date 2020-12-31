package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

@TeleOp(name = "WHS TeleOp", group = "TeleOp")
public class WHSTeleOp extends OpMode {
     WHSRobotImpl robot;

    public Toggler binToggler = new Toggler(3);
    public Toggler shootingTimerTog = new Toggler (3);

    public Outtake.GoalPositions currentTarget;
    public Position currentTargetPos;

    public SimpleTimer rotateTimer = new SimpleTimer();

    public final int ROTATE_TIME = 500;

    public String  currentTargetWord;
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
        if (gamepad1.a){
            robot.intake.manualDropdown(gamepad1.a);
        }
        //Canister
        if (gamepad2.x || gamepad2.y) {
            robot.canister.operateLoader(gamepad2.x);
            robot.canister.operatePlatform(gamepad2.y);
        }
        else {
            robot.canister.operateLoader(false);
            robot.canister.operatePlatform(false);
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
        binToggler.changeState(gamepad2.dpad_right, gamepad2.dpad_left);
        if (gamepad2.dpad_left){
            currentTargetWord = "Left Powershot";
            currentTarget = Outtake.GoalPositions.LEFT_POWER_SHOT;
            currentTargetPos = powershot1;
        }
        else if (gamepad2.dpad_up){
            currentTargetWord = "Center Powershot";
            currentTarget = Outtake.GoalPositions.CENTER_POWER_SHOT;
            currentTargetPos = powershot2;
        }
        else if (gamepad2.dpad_right){
            currentTargetWord = "Right Powershot";
            currentTarget = Outtake.GoalPositions.RIGHT_POWER_SHOT;
            currentTargetPos = powershot3;

        }
        else if (gamepad2.dpad_down){
            currentTargetPos = binsMidpoint;
            currentTarget = Outtake.GoalPositions.LOW_BIN;
            currentTargetWord = "Low Bin";

        }
        else if(gamepad2.left_bumper){
            currentTargetPos = binsMidpoint;
            currentTarget = Outtake.GoalPositions.MEDIUM_BIN;
            currentTargetWord = "Medium Bin";
        }
        else if (gamepad2.right_bumper){
            currentTargetPos = binsMidpoint;
            currentTarget = Outtake.GoalPositions.HIGH_BIN;
            currentTargetWord = "High Bin";
        }

        if (gamepad2.a){
            if (shootingTimerTog.currentState() == 0){
                rotateTimer.set(ROTATE_TIME);
                shootingTimerTog.changeState(true);
            } else if (shootingTimerTog.currentState() == 1){
                robot.rotateToTarget(robot.outtake.calculateLaunchHeading(currentTargetPos, robot.getCoordinate()), false);
                if (rotateTimer.isExpired()){
                    shootingTimerTog.changeState(true);
                }
            } else {
                robot.outtake.launchToTarget(currentTarget);
                if (robot.outtake.launchTimer.isExpired()){
                    shootingTimerTog.changeState(true);
                }
            }
        }

        //Wobble - currently redoing wobble
        if(gamepad1.dpad_up){
            robot.wobble.operateArm(gamepad1.dpad_up);
        }
        if (gamepad1.dpad_down){
            robot.wobble.operateClaw(gamepad1.dpad_down);
        }
        telemetry.addData("Intake State: ", intakeStatus);
        telemetry.addData("Intake Position",robot.intake.dropdownStatus);
        telemetry.addData("Canister Loader State: ", robot.canister.canisterState);
        telemetry.addData("Canister Platform Orientation: ", robot.canister.platformState);
        telemetry.addData("Robot Current Position: ", robot.drivetrain.getAllEncoderPositions());
        telemetry.addData("Current Target: ", currentTargetWord);
        telemetry.addData("Wobble Arm State: ", robot.wobble.armStateDescription);
        telemetry.addData("Wobble Claw State: ", robot.wobble.clawStateDescription);
    }
}
