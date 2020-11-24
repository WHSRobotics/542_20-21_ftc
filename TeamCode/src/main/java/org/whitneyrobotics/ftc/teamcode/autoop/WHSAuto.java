package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwerveToTarget;
import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;
import org.whitneyrobotics.ftc.teamcode.subsys.Wobble;

//import static org.whitneyrobotics.ftc.teamcode.subsys.Outtake.Off;

public class WHSAuto extends OpMode {
    WHSRobotImpl robot;

    static final int RED = 0;
    static final int BLUE = 1;
    static final int INSIDE = 0;
    static final int OUTSIDE = 1;

    static final int STARTING_POSITION = INSIDE;
    public static final int STARTING_ALLIANCE = RED;
    static final double STARTING_COORDINATE_X = 1200;
    static final double STARTING_COORDINATE_Y = -1800;
    static final boolean PARTNER_MOVED_WOBBLE = false;

    static final double LEFT_MAX = 80;
    static final double CENTER_MAX = 165;

    static final int LEFT = 0;
    static final int CENTER = 1;
    static final int RIGHT = 2;

    public static int wobblePosition = 0; //placeholder
    int powerShotPosition = CENTER;

    Coordinate[] startingCoordinateArray = new Coordinate [2];//starting coordinate
    Position[][] scanningDistanceArray = new Position[2][2];//scanning diatances
    Position[] shootingPositionArray = new Position[2];// points whrere robot sits to shoot powershots
    Position[][] wobblePositionArray = new Position[2][3];// wobble boxes
    Position[][] parkingPositionArray = new Position[2][3];//parking spots
    Position[] ringPosition = new Position[2];//ring stack postions

    SwerveToTarget driveToShotLineSwerve;
    SwerveToTarget driveToWobblePositionOneSwerve;
    SwerveToTarget driveToWobblePositionTwoSwerve;
    SwerveToTarget driveToWobblePositionThreeSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleOneSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleTwoSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleThreeSwerve;

    private void instantiateSwerveToTargets() {
        Position [] driveToShotLineSwervePositions = {scanningDistanceArray[STARTING_ALLIANCE][STARTING_POSITION], shootingPositionArray[STARTING_ALLIANCE]};
        Position [] driveToWobblePositionOneSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][0]};
        Position [] driveToWobblePositionTwoSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][1]};
        Position [] driveToWobblePositionThreeSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][2]};
        Position [] driveToLaunchLineFromWobbleOneSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][0], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
        Position [] driveToLaunchLineFromWobbleTwoSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][1], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
        Position [] driveToLaunchLineFromWobbleThreeSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][2], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
    }

  //insert Swerve to Target Here
  final double STRAFE_TO_RING_LAUNCH_POWER = 0.7542;

    //State definitions
    static final int INIT = 0;
    static final int SCAN_STACK = 1;
    static final int DRIVE_TO_LAUNCH_POINT = 2;
    static final int LAUNCH_PARTICLES = 3;
    static final int DROP_OFF_WOBBLE_GOAL = 4;
    static final int PARK_ON_LAUNCH_LINE = 5;
    static final int END = 6;

    static final int NUMBER_OF_STATES = 7;

    boolean[] stateEnabled = new boolean[NUMBER_OF_STATES];

    int state = INIT;
    int subState = 0;
    int wobblePickupState = 0;
    String outtakeState = "hover";
    public void advanceState() {
        if (stateEnabled[(state + 1)]) {
            state++;
            subState = 0;
        } else {
            state++;
            advanceState();
        }
    }

        public void defineStateEnabledStatus() {
            stateEnabled[INIT] = true;
            stateEnabled[SCAN_STACK] = true;
            stateEnabled[DRIVE_TO_LAUNCH_POINT] = true;
            stateEnabled[LAUNCH_PARTICLES] = true;
            stateEnabled[DROP_OFF_WOBBLE_GOAL] = true;
            stateEnabled[PARK_ON_LAUNCH_LINE] = true;
            stateEnabled[END] = true;
        }

        //timers

        SimpleTimer scannerTimer = new SimpleTimer();
        SimpleTimer driveToLaunchTimer = new SimpleTimer();
        SimpleTimer launchTimer1 = new SimpleTimer();
        SimpleTimer launchTimer2 = new SimpleTimer();
        SimpleTimer launchTimer3 = new SimpleTimer();
        SimpleTimer powershot1Timer = new SimpleTimer();
        SimpleTimer powershot2Timer = new SimpleTimer();
        SimpleTimer powershot3Timer = new SimpleTimer();
        SimpleTimer driveToWobbleOneTimer = new SimpleTimer();
        SimpleTimer driveToWobbleTwoTimer = new SimpleTimer();
        SimpleTimer driveToWobbleThreeTimer = new SimpleTimer();
        SimpleTimer putDownWobble = new SimpleTimer();
        SimpleTimer driveToLaunchLineFromWobbleOne = new SimpleTimer();
        SimpleTimer driveToLaunchLineFromWobbleTwo = new SimpleTimer();
        SimpleTimer driveToLaunchLineFromWobbleThree = new SimpleTimer();
        SimpleTimer stopAutoOP = new SimpleTimer();

    private final double DRIVE_TO_LAUNCH = 1.0;
    private final double LAUNCH_DELAY = 1.0;
    private final double DRIVE_TO_WOBBLE_ONE_DELAY = 1.0;
    private final double DRIVE_TO_WOBBLE_TWO_DELAY = 1.0;
    private final double DRIVE_TO_WOBBLE_THREE_DELAY = 1.0;
    private final double PUT_DOWN_WOBBLE = 1.5;
    private final double DRIVE_TO_LAUNCH_FROM_WOBBLE_ONE_DELAY = 1.0;
    private final double DRIVE_TO_LAUNCH_FROM_WOBBLE_TWO_DELAY = 1.0;
    private final double DRIVE_TO_LAUNCH_FROM_THREE_ONE_DELAY = 1.0;
    private final double STOP_AT_LAUNCH_DELAY = 0.75;

    double[] motorPowers = {0.0, 0.0};



    @Override
    public void init() {
            robot = new WHSRobotImpl(hardwareMap);
            robot.drivetrain.resetEncoders();
            defineStateEnabledStatus();

        startingCoordinateArray[RED] = new Coordinate(STARTING_COORDINATE_X, -1571, 90);

        //all coordinates here are placeholders, change later
        scanningDistanceArray[RED][INSIDE] = new Position(1, -2);
        shootingPositionArray[RED] =new Position(3, -4);
        wobblePositionArray[STARTING_ALLIANCE][0] = new Position(5,-6);
        wobblePositionArray[STARTING_ALLIANCE][1] = new Position(7,-8);
        wobblePositionArray[STARTING_ALLIANCE][2] = new Position(9, -10);
        parkingPositionArray[RED][wobblePosition] = new Position(11,-12);
        ringPosition[RED] = new Position(13, -14);
        startingCoordinateArray[INSIDE] = new Coordinate (-1800, -600, 0);
        startingCoordinateArray[OUTSIDE] = new Coordinate (-1800, -900, 0);


        instantiateSwerveToTargets();
        robot.setInitialCoordinate(startingCoordinateArray[STARTING_ALLIANCE]);
    }

    @Override
    public void init_loop() {

      /*  if (robot.wobbleHeightDetector.getScreenPosition().x < LEFT_MAX) {
            autoOpRingPosition = 2;
        } else if (robot.wobbleHeightDetector.getScreenPosition().x < CENTER_MAX) {
            autoOpRingPosition = 1;
        } else {
            autoOpRingPosition = 0;
        }*/
    }
    public String newOuttakeState = "hover";
    public String stateDesc = "";
    public String subStateDesc = "";

    @Override
    public void loop() {
        robot.estimateHeading();
        robot.estimatePosition();

       /* switch (newOuttakeState) {
            case "hover":
                robot.wobble.setArmPosition(Wobble.ArmPositions.UP);
                robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                break;
            case "grab":
                robot.wobble.setArmPosition(Wobble.ArmPositions.OVER);
                robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                robot.wobble.setArmPosition(Wobble.ArmPositions.UP);
                break;
            case "outtake1":
                //robot.rotateToTarget(robot.outtake.calculateLaunchHeading(robot.outtake.powershot1, robot.getCoordinate()), false);
                robot.newouttake.setFlapServoPositions(Outtake.GoalPositions.POWER_SHOT_TARGET_ONE);
                //robot.outtake.autoOuttake(1);
                break;
            case "outtake2":
                //robot.outtake.autoOuttake(2);
                break;
            default:
                break;
        }*/

        switch (state) {
            case INIT:
                stateDesc = "Starting auto";
                switch(subState){
                    case 0:
                        subStateDesc = "Lower Arm";
                        robot.wobble.setArmPosition(Wobble.ArmPositions.DOWN);
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Clinch Wobble";
                        robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                        subState++;
                        break;
                    case 2:
                        subStateDesc = "Raise Arm";
                        robot.wobble.setArmPosition(Wobble.ArmPositions.UP);
                        subState++;
                        break;
                }
                robot.intake.setDropdown(Intake.DropdownPositions.DOWN);
                advanceState();
                break;
            case DRIVE_TO_LAUNCH_POINT:
                stateDesc = "Scan Stack";
                robot.driveToTarget(robot.outtake.launchPoint, false);
                advanceState();
                break;
            case SCAN_STACK:
                stateDesc = "Driving to the Launch Point";
                switch (subState) {
                    case 0:
                        subStateDesc = "scanStack";
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Rotating to Launch Heading";
                        motorPowers = driveToShotLineSwerve.calculateMotorPowers(robot.getCoordinate(), robot.drivetrain.getWheelVelocities(), true);
                        if (!driveToShotLineSwerve.inProgress()) {
                            subState++;
                        }
                        break;
                }
                advanceState();
                break;
            case LAUNCH_PARTICLES:
                stateDesc = "Ready to Launch";
                switch (subState) {
                    case 0:
                        subStateDesc = "Load Ring and Aim Powershot1";
                        robot.canister.loadRing();
                        robot.outtake.flap.setPosition(robot.outtake.calculateLaunchSetting(robot.outtake.calculateDistanceToTarget(robot.outtake.powershot1, robot.getCoordinate()), robot.outtake.POWER_SHOT_TARGET_HEIGHT ));
                        robot.rotateToTarget(robot.outtake.calculateLaunchHeading(robot.outtake.powershot1, robot.getCoordinate()), false);
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Shoot Powershot1";
                        launchTimer1.set(500);
                        while (!launchTimer1.isExpired()) {
                            robot.outtake.setLauncherPower(robot.outtake.FLYWHEEL_POWER);
                        }
                        robot.outtake.setLauncherPower(0);
                        subState++;
                        break;
                    case 2:
                        subStateDesc = "Load Ring and Aim Powershot2";
                        robot.canister.loadRing();
                        robot.outtake.flap.setPosition(robot.outtake.calculateLaunchSetting(robot.outtake.calculateDistanceToTarget(robot.outtake.powershot2, robot.getCoordinate()), robot.outtake.POWER_SHOT_TARGET_HEIGHT ));
                        robot.rotateToTarget(robot.outtake.calculateLaunchHeading(robot.outtake.powershot2, robot.getCoordinate()), false);
                        subState++;
                        break;
                    case 3:
                        subStateDesc = "Shoot Powershot2";
                        launchTimer2.set(500);
                        while (!launchTimer2.isExpired()) {
                            robot.outtake.setLauncherPower(robot.outtake.FLYWHEEL_POWER);
                        }
                        robot.outtake.setLauncherPower(0);
                        subState++;
                        break;
                    case 4:
                        subStateDesc = "Load Ring and Aim Powershot3";
                        robot.canister.loadRing();
                        robot.outtake.flap.setPosition(robot.outtake.calculateLaunchSetting(robot.outtake.calculateDistanceToTarget(robot.outtake.powershot3, robot.getCoordinate()), robot.outtake.POWER_SHOT_TARGET_HEIGHT ));
                        robot.rotateToTarget(robot.outtake.calculateLaunchHeading(robot.outtake.powershot3, robot.getCoordinate()), false);
                        subState++;
                        break;
                    case 5:
                        subStateDesc = "Shoot Powershot3";
                        launchTimer3.set(500);
                        while (!launchTimer3.isExpired()) {
                            robot.outtake.setLauncherPower(robot.outtake.FLYWHEEL_POWER);
                        }
                        robot.outtake.setLauncherPower(0);
                        subState++;
                }
                advanceState();
                break;
            case DROP_OFF_WOBBLE_GOAL:
                stateDesc = "Scoring wobble goal";
                switch (subState) {
                    case 0:
                       subStateDesc="Move to Wobble Box";
                       // Write code after camera
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Lower Arm and Realease";
                        robot.wobble.setArmPosition(Wobble.ArmPositions.DOWN);
                        robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                        subState++;
                        break;
                    case 2:
                        subStateDesc = "Raise Arm";
                        robot.wobble.setArmPosition(Wobble.ArmPositions.UP);
                        subState++;
                        break;
                    /*case "score":
                        robot.wobble.setArmPosition(Wobble.ArmPositions.OVER);
                        robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                        break;
                    default:
                        break;*/
                }
                advanceState();
                break;

            case PARK_ON_LAUNCH_LINE:
                stateDesc = "Park";
                Position parkingSpot = new Position( 300 ,robot.getCoordinate().getY());
                robot.driveToTarget(parkingSpot, false);
               /* stateDesc = "Driving to foundation";
                switch (subState) {
                    case 0:
                        subStateDesc = "Entry";
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Driving to foundation";
                        if (stateEnabled[SECONDARY_MOVE_FOUNDATION]) {
                            motorPowers = skystoneToUnmovedFoundationSwerve.calculateMotorPowers(robot.getCoordinate(), robot.drivetrain.getWheelVelocities(), true);
                        } else {
                            motorPowers = skystoneToMovedFoundationSwerve.calculateMotorPowers(robot.getCoordinate(), robot.drivetrain.getWheelVelocities(), STARTING_ALLIANCE == BLUE);
                        }
                        if (robot.getCoordinate().getX() > 300) {
                            outtakeState = "outtake1";
                        }
                        if (!skystoneToUnmovedFoundationSwerve.inProgress() && !skystoneToMovedFoundationSwerve.inProgress()) {
                            subState++;
                        }
                        break;
                    case 2:
                        subStateDesc = "Exit";
                        advanceState();
                        break;
                }
                break;
            case OUTTAKE_SKYSTONE:
                stateDesc = "Outtaking skystone";
                switch (subState) {
                    case 0:
                        subStateDesc = "Entry";
                        foundationPullerUpToDownTimer.set(GRAB_FOUNDATION_DELAY);
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Moving foundation pullers";
                        robot.foundationPuller.setFoundationPullerPosition(FoundationPuller.PullerPosition.DOWN);
                        operatingFoundationPullers = true;
                        if (foundationPullerUpToDownTimer.isExpired()) {
                            subState++;
                        }
                        break;
                    case 2:
                        subStateDesc = "Outtaking skystone and pulling foundation";
                        robot.intake.setVelocity(-Intake.INTAKE_VELOCITY);
                        motorPowers = foundationToWallSwerve.calculateMotorPowers(robot.getCoordinate(), robot.drivetrain.getWheelVelocities(), false);
                        if (!foundationToWallSwerve.inProgress()) {
                            robot.intake.setVelocity(0);
                            subState++;
                        }
                        break;
                    case 3:
                        subStateDesc = "rotating";
                        if (STARTING_ALLIANCE == BLUE){
                            robot.rotateToTarget(-160,false);
                        }else if (STARTING_ALLIANCE == RED){
                            robot.rotateToTarget(160, false);
                        }
                        if (!robot.rotateToTargetInProgress()){
                            robot.foundationPuller.setFoundationPullerPosition(FoundationPuller.PullerPosition.UP);
                        }
                        if (!robot.rotateToTargetInProgress()){
                            subState++;
                        }
                        break;
                    case 4:
                        subStateDesc = "Exit";
                        advanceState();
                        break;
                }*/
                break;
            case END:
                stateDesc = "Ending Auto";
                robot.intake.setDropdown(Intake.DropdownPositions.UP);
        }
        telemetry.addData("State: ", stateDesc);
        telemetry.addData("Substate: ", subStateDesc);
        telemetry.addData("IMU", robot.imu.getHeading());
        //telemetry.addData("Stone Sensed?", robot.intake.stoneSensed());
        telemetry.addData("X", robot.getCoordinate().getX());
        telemetry.addData("Y", robot.getCoordinate().getY());
        telemetry.addData("Heading", robot.getCoordinate().getHeading());
        /*Telemetry.Item stone_position_x = telemetry.addData("Stone Position X", robot.skystoneDetector.getScreenPosition().x);
        telemetry.addData("Stone Position Y", robot.skystoneDetector.getScreenPosition().y);
        telemetry.addData("Frame Count", robot.webcam.getFrameCount());
        telemetry.addData("FPS", String.format(Locale.US, "%.2f", robot.webcam.getFps()));
        telemetry.addData("Total frame time ms", robot.webcam.getTotalFrameTimeMs());
        telemetry.addData("Pipeline time ms", robot.webcam.getPipelineTimeMs());
        telemetry.addData("Overhead time ms", robot.webcam.getOverheadTimeMs());
        telemetry.addData("Theoretical max FPS", robot.webcam.getCurrentPipelineMaxFps());
        telemetry.addData("Skystone Position", skystonePosition);
        telemetry.addData("Auto Ring Position: ", autoOpRingPosition);*/
    }

    @Override
    public void stop() {
        //robot.intake.setIntakePusherPosition(Intake.IntakePusherPosition.DOWN);
    }
}

