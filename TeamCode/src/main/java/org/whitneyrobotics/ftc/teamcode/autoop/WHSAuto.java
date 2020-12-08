package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwerveToTarget;
import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;
import org.whitneyrobotics.ftc.teamcode.subsys.Wobble;

//import static org.whitneyrobotics.ftc.teamcode.subsys.Outtake.Off;

@Autonomous(name = "WHS Auto", group = "Auto")
public class WHSAuto extends OpMode {
    WHSRobotImpl robot;

    // Starting color and inside/outside array values
    static final int RED = 0;
    static final int BLUE = 1;
    static final int INSIDE = 0;
    static final int OUTSIDE = 1;

    // Starting information
    static final int STARTING_POSITION = INSIDE;
    public static final int STARTING_ALLIANCE = RED;
    static final double STARTING_COORDINATE_X = 1200;
    static final double STARTING_COORDINATE_Y = -1800;
    static final boolean PARTNER_MOVED_WOBBLE = false;

    // ?? Anyone who knows... comment this?
    static final double LEFT_MAX = 80;
    static final double CENTER_MAX = 165;

    /*static final int LEFT = 0;
    static final int CENTER = 1;
    static final int RIGHT = 2;*/

    public static int wobblePosition = 0; //placeholder

    public Position launchPoint = new Position(300, -285.75);// optimize during testing
    public final Position powershot1 = new Position(1800,-95.25); // from right to left fix later
    public final Position powershot2 = new Position(1800,-285.75);
    public final Position powershot3 = new Position(1800,-476.25);
    public final Position binsMidpoint = new Position(1800,-890.5875);

    Coordinate[] startingCoordinateArray = new Coordinate [2];//starting coordinate

    Position[] shootingPositionArray = new Position[2];// points whrere robot sits to shoot powershots
    Position[] ringPosition = new Position[2];//ring stack postions

    Position[][] scanningDistanceArray = new Position[2][2];//scanning diatances
    Position[][] wobblePositionArray = new Position[2][3];// wobble boxes
    Position[][] parkingPositionArray = new Position[2][3];//parking spots

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
    static final int PARK_ON_STARTING_LINE = 5;
    static final int END = 6;

    static final int NUMBER_OF_STATES = 7;

    boolean[] stateEnabled = new boolean[NUMBER_OF_STATES];

    int state = INIT;
    int subState = 0;

    /*int wobblePickupState = 0;
    String outtakeState = "hover";*/

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
            stateEnabled[PARK_ON_STARTING_LINE] = true;
            stateEnabled[END] = true;
        }

        //timers
        SimpleTimer scannerTimer = new SimpleTimer(); // implement in SCAN STACK code
        SimpleTimer wobblePickupArmDownTimer = new SimpleTimer();
        SimpleTimer wobblePickupClawCloseTimer = new SimpleTimer();
        SimpleTimer wobbleArmRaiseTimer = new SimpleTimer();
        SimpleTimer dropDownTimer = new SimpleTimer();
        SimpleTimer leftPowershotAimTimer = new SimpleTimer();
        SimpleTimer centerPowershotAimTimer = new SimpleTimer();
        SimpleTimer rightPowershotAimTimer = new SimpleTimer();
        SimpleTimer putDownWobble = new SimpleTimer();
        SimpleTimer stopAutoOP = new SimpleTimer();
        SimpleTimer resetDropdownTimer = new SimpleTimer();

    //test all of these
    private final double WOBBLE_PICKUP_ARM_DOWN_DELAY = 1000.0; // optimize in testing
    private final double WOBBLE_PICKUP_CLAW_CLOSE_DELAY = 1000.0; // optimize in testing
    private final double WOBBLE_ARM_RAISE_DELAY = 1000.0;
    private final double DROPDOWN_DELAY = 1000.0; //test
    private final double POWERSHOT_AIM_DELAY = 1000.0;
    private final double PUT_DOWN_WOBBLE_DELAY = 1000.0; // optimize in testing
    private final double STOP_AUTOOP_DELAY = 1000.0;
    private final double RESET_DROPDOWN_DELAY = 1000.0;


    //private final double SECONDS_TO_MILLISECONDS = 1000.0;

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
                        wobblePickupArmDownTimer.set(WOBBLE_PICKUP_ARM_DOWN_DELAY );
                        while (!wobblePickupArmDownTimer.isExpired()) {
                            robot.wobble.setArmPosition(Wobble.ArmPositions.DOWN);
                        }
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Clinch Wobble";
                        wobblePickupClawCloseTimer.set(WOBBLE_PICKUP_CLAW_CLOSE_DELAY);
                        while (!wobblePickupClawCloseTimer.isExpired()) {
                            robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                        }
                        subState++;
                        break;
                    case 2:
                        subStateDesc = "Raise Arm";
                        wobbleArmRaiseTimer.set(WOBBLE_ARM_RAISE_DELAY);
                        while (!wobbleArmRaiseTimer.isExpired()) {
                            robot.wobble.setArmPosition(Wobble.ArmPositions.UP);
                        }
                        subState++;
                        break;
                    case 3:
                        subStateDesc = "Dropping Intake";
                        dropDownTimer.set(DROPDOWN_DELAY);
                        while (!dropDownTimer.isExpired()) {
                            robot.intake.setDropdown(Intake.DropdownPositions.DOWN);
                        }
                        break;
                    default:
                        break;
                }
                advanceState();
                break;
            case SCAN_STACK:
                stateDesc = "Scan Stack";
                switch (subState) {
                    case 0:
                        subStateDesc = "Scan Stack";
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Rotating to Launch Heading";
                        motorPowers = driveToShotLineSwerve.calculateMotorPowers(robot.getCoordinate(), robot.drivetrain.getWheelVelocities(), true);
                        if (!driveToShotLineSwerve.inProgress()) {
                            subState++;
                        }
                        break;
                    default:
                        break;
                }
                advanceState();
                break;
            case DRIVE_TO_LAUNCH_POINT:
                // make this a swerve to target
                stateDesc = "Driving to the Launch Point";
                robot.driveToTarget(launchPoint, false);
                advanceState();
                break;
            case LAUNCH_PARTICLES:
                stateDesc = "Ready to Launch";
                switch (subState) {
                    case 0:
                        subStateDesc = "Load Ring and Aim Left Powershot";
                        robot.canister.loadRing();
                        leftPowershotAimTimer.set(POWERSHOT_AIM_DELAY);
                        while (!leftPowershotAimTimer.isExpired()) {
                            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(powershot1, robot.getCoordinate()), false);
                        }
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Shoot Left Powershot";
                        robot.outtake.launchToTarget(Outtake.GoalPositions.LEFT_POWER_SHOT);
                        subState++;
                        break;
                    case 2:
                        subStateDesc = "Load Ring and Aim Center Powershot";
                        robot.canister.loadRing();
                        centerPowershotAimTimer.set(POWERSHOT_AIM_DELAY);
                        while (!centerPowershotAimTimer.isExpired()) {
                            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(powershot2, robot.getCoordinate()), false);
                        }
                        subState++;
                        break;
                    case 3:
                        subStateDesc = "Shoot Center Powershot";
                        robot.outtake.launchToTarget(Outtake.GoalPositions.CENTER_POWER_SHOT);
                        subState++;
                        break;
                    case 4:
                        subStateDesc = "Load Ring and Aim Right Powershot";
                        robot.canister.loadRing();
                        rightPowershotAimTimer.set(POWERSHOT_AIM_DELAY);
                        while(!rightPowershotAimTimer.isExpired()) {
                            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(powershot3, robot.getCoordinate()), false);
                        }
                        subState++;
                        break;
                    case 5:
                        subStateDesc = "Shoot Right Powershot";
                        robot.outtake.launchToTarget(Outtake.GoalPositions.RIGHT_POWER_SHOT);
                        break;
                    default:
                        break;
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
                        putDownWobble.set(PUT_DOWN_WOBBLE_DELAY);
                        if (!putDownWobble.isExpired()) {
                            robot.wobble.setArmPosition(Wobble.ArmPositions.DOWN);
                            robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                        }
                        subState++;
                        break;
                    case 2:
                        subStateDesc = "Raise Arm";
                        robot.wobble.setArmPosition(Wobble.ArmPositions.FOLDED);
                        break;

                    default:
                        break;
                        /*case "score":
                        robot.wobble.setArmPosition(Wobble.ArmPositions.OVER);
                        robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                        break;*/
                }
                advanceState();
                break;

            case PARK_ON_STARTING_LINE:
                stateDesc = "Park";
                Position parkingSpot = new Position( 300 ,robot.getCoordinate().getY());
                // make swerve to target
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
                stopAutoOP.set(STOP_AUTOOP_DELAY);
                if (!stopAutoOP.isExpired()) {
                    advanceState();
                }
                break;
            case END:
                stateDesc = "Ending Auto";
                resetDropdownTimer.set(RESET_DROPDOWN_DELAY);
                while (!resetDropdownTimer.isExpired()) {
                    robot.intake.setDropdown(Intake.DropdownPositions.UP);
                }
                break;
            default:
                break;
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

