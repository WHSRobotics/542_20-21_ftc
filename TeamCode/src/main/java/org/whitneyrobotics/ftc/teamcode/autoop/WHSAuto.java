package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
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
    static final double STARTING_COORDINATE_X = -1500;
    static final double STARTING_COORDINATE_Y = -600;
    static final boolean PARTNER_MOVED_WOBBLE = false;

    // ?? Anyone who knows... comment this?
    static final double LEFT_MAX = 80;
    static final double CENTER_MAX = 165;

    /*static final int LEFT = 0;
    static final int CENTER = 1;
    static final int RIGHT = 2;*/

    public int wobblePosition = 0; //placeholder

    //public Position launchPoint = new Position(300, -285.75);// optimize during testing
    public final Position powershot1 = new Position(1800, -95.25); // from right to left fix later
    public final Position powershot2 = new Position(1800, -285.75);
    public final Position powershot3 = new Position(1800, -476.25);
    public final Position binsMidpoint = new Position(1800, -890.5875);

    Coordinate[] startingCoordinateArray = new Coordinate[2];//starting coordinate

    Position[] shootingPositionArray = new Position[2];// points whrere robot sits to shoot powershots
    Position[] ringPosition = new Position[2];//ring stack postions

    Position[][] scanningDistanceArray = new Position[2][2];//scanning diatances
    Position[][] wobblePositionArray = new Position[2][3];// wobble boxes
    Position[][] parkingPositionArray = new Position[2][2];//parking spots

    /*
    SwerveToTarget driveToShotLineSwerve;
    SwerveToTarget driveToWobblePositionOneSwerve;
    SwerveToTarget driveToWobblePositionTwoSwerve;
    SwerveToTarget driveToWobblePositionThreeSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleOneSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleTwoSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleThreeSwerve;
     */

    /*
    SwervePath startToShotline;
    SwervePath shotLineToWobbleOne;
    SwervePath shotlineToWobbleTwo;
    SwervePath shotlineToWobbleThree;
    SwervePath wobbleOneToParkline;
    SwervePath wobbleTwoToParkline;
    SwervePath wobbleThreeToParkline;
    */

    /*
    // Initialize FollowerConstants
    FollowerConstants startToShotlineFollowerConstants = new FollowerConstants(AutoSwervePositions.startToShotlineLookaheadDist, false);
    FollowerConstants shotlineToWobbleOneFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble1LookaheadDist, false);
    FollowerConstants shotlineToWobbleTwoFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble2LookahadDist, false);
    FollowerConstants shotlineToWobbleThreeFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble3LookaheadDist, false);
    FollowerConstants wobbleOneToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble1ToParkLookaheadDist, true);
    FollowerConstants wobbleTwoToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble2ToParkLookaheadDist, true);
    FollowerConstants wobbleThreeToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble3ToParkLookaheadDist, true);

    // Initialize SwervePathGenerationConstants
    SwervePathGenerationConstants startToShotlinePathGenConstants = new SwervePathGenerationConstants(AutoSwervePositions.startToShotlineWaypointSpacing, AutoSwervePositions.startToShotlineWeightSmooth, AutoSwervePositions.startToShotlineTurnSpeed, AutoSwervePositions.startToShotlineMaxVelocity);
    SwervePathGenerationConstants shotlineToWobbleOneGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble1Spacing, AutoSwervePositions.shotlineToWobble1WeightSmooth, AutoSwervePositions.shotlineToWobble1TurnSpeed, AutoSwervePositions.shotlineToWobble1MaxVelocity);
    SwervePathGenerationConstants shotlineToWobbleTwoGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble2Spacing, AutoSwervePositions.shotlineToWobble2WeightSmooth, AutoSwervePositions.shotlineToWobble2TurnSpeed, AutoSwervePositions.shotlineToWobble2MaxVelocity);
    SwervePathGenerationConstants shotlineToWobbleThreeGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble3Spacing, AutoSwervePositions.shotlineToWobble3WeightSmooth, AutoSwervePositions.shotlineToWobble3TurnSpeed, AutoSwervePositions.shotlineToWobble3MaxVelocity);
    SwervePathGenerationConstants wobbleOneToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble1ToParkSpacing, AutoSwervePositions.wobble1ToParkWeightSmooth, AutoSwervePositions.wobble1ToParkTurnSpeed, AutoSwervePositions.wobble1ToParkMaxVelocity);
    SwervePathGenerationConstants wobbleTwoToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble2ToParkSpacing, AutoSwervePositions.wobble2ToParkWeightSmooth, AutoSwervePositions.wobble2ToParkTurnSpeed, AutoSwervePositions.wobble2ToParkMaxVelocity);
    SwervePathGenerationConstants wobbleThreeToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble3ToParkSpacing, AutoSwervePositions.wobble3ToParkWeightSmooth, AutoSwervePositions.wobble3ToParkTurnSpeed, AutoSwervePositions.wobble3ToParkMaxVelocity);
    */

    private void instantiateSwerveToTargets() {
        Position[] driveToShotLineSwervePositions = {scanningDistanceArray[STARTING_ALLIANCE][STARTING_POSITION], shootingPositionArray[STARTING_ALLIANCE]};
        Position[] driveToWobblePositionOneSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][0]};
        Position[] driveToWobblePositionTwoSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][1]};
        Position[] driveToWobblePositionThreeSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][2]};
        Position[] driveToLaunchLineFromWobbleOneSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][0], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
        Position[] driveToLaunchLineFromWobbleTwoSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][1], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
        Position[] driveToLaunchLineFromWobbleThreeSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][2], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
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
    SimpleTimer wobbleExtendTimer = new SimpleTimer();
    SimpleTimer wobblePickupArmDownTimer = new SimpleTimer();
    SimpleTimer wobblePickupClawCloseTimer = new SimpleTimer();
    SimpleTimer dropDownTimer = new SimpleTimer();
    SimpleTimer leftPowershotAimTimer = new SimpleTimer();
    SimpleTimer centerPowershotAimTimer = new SimpleTimer();
    SimpleTimer rightPowershotAimTimer = new SimpleTimer();
    SimpleTimer putDownWobble = new SimpleTimer();
    SimpleTimer wobbleFoldTimer = new SimpleTimer();
    SimpleTimer resetDropdownTimer = new SimpleTimer();

    //test all of these
    private final double WOBBLE_EXTEND_DELAY = 1000.0 ;// optimize in testing
    private final double WOBBLE_PICKUP_CLAW_CLOSE_DELAY = 1000.0; // optimize in testing
    private final double DROPDOWN_DELAY = 1000.0; //test
    private final double POWERSHOT_AIM_DELAY = 1000.0;
    private final double PUT_DOWN_WOBBLE_DELAY = 1000.0; // optimize in testing
    private final double WOBBLE_FOLD_DELAY = 1000.0;
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
        shootingPositionArray[RED] = new Position(3, -4);
        wobblePositionArray[STARTING_ALLIANCE][0] = new Position(5, -6);
        wobblePositionArray[STARTING_ALLIANCE][1] = new Position(7, -8);
        wobblePositionArray[STARTING_ALLIANCE][2] = new Position(9, -10);
        parkingPositionArray[RED][wobblePosition] = new Position(11, -12);
        ringPosition[RED] = new Position(13, -14);
        startingCoordinateArray[INSIDE] = new Coordinate(-1800, -600, 0);
        startingCoordinateArray[OUTSIDE] = new Coordinate(-1800, -900, 0);


        instantiateSwerveToTargets();
        robot.setInitialCoordinate(startingCoordinateArray[STARTING_ALLIANCE]);

        /*
        // intit swerveToTargets
        startToShotline = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.startToShotlinePath), startToShotlineFollowerConstants, startToShotlinePathGenConstants);
        shotLineToWobbleOne = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble1Path), shotlineToWobbleOneFollowerConstants, shotlineToWobbleOneGenerationConstants);
        shotlineToWobbleTwo = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble2Path), shotlineToWobbleTwoFollowerConstants, shotlineToWobbleTwoGenerationConstants);
        shotlineToWobbleThree = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble3Path), shotlineToWobbleThreeFollowerConstants, shotlineToWobbleThreeGenerationConstants);
        wobbleOneToParkline = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble1ToParkPath), wobbleOneToParkFollowerConstants, wobbleOneToParkGenerationConstants);
        wobbleTwoToParkline = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble2ToParkPath), wobbleTwoToParkFollowerConstants, wobbleTwoToParkGenerationConstants);
        wobbleThreeToParkline = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble3ToParkPath), wobbleThreeToParkFollowerConstants, wobbleThreeToParkGenerationConstants);
        */
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
                switch (subState) {
                    case 0:
                        subStateDesc = "Set Wobble Extending Timer";
                        wobbleExtendTimer.set(WOBBLE_EXTEND_DELAY);
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Extend Wobble";
                        while (!wobblePickupArmDownTimer.isExpired()) {
                            robot.wobble.setLinearSlidePosition(Wobble.LinearSlidePositions.DOWN);
                            robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.OUT);
                            robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                        }
                        subState++;
                        break;
                    case 2:
                        subStateDesc = "Set Wobble Claw Close Timer";
                        wobblePickupClawCloseTimer.set(WOBBLE_PICKUP_CLAW_CLOSE_DELAY);
                        subState++;

                    case 3:
                        subStateDesc = "Clinch Wobble";
                        while (!wobblePickupClawCloseTimer.isExpired()) {
                            robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                            robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.IN);
                        }
                        subState++;
                        break;
                    case 4:
                        subStateDesc = "Set drop Intake Timer";
                        dropDownTimer.set(DROPDOWN_DELAY);

                    case 5:
                        subStateDesc = "Dropping Intake";
                        while (!dropDownTimer.isExpired()) {
                            robot.intake.setDropdown(Intake.DropdownPositions.DOWN);
                        }
                        break;
                    case 6:
                        subStateDesc = "Exit";
                        advanceState();
                    default:
                        break;
                }
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
                        subState++;
                        break;
                    default:
                        break;
                }
                advanceState();
                break;
            case DRIVE_TO_LAUNCH_POINT:
                stateDesc = "Driving to the Launch Point";
                robot.updatePath(AutoSwervePositions.generateAutoPaths(AutoSwervePositions.startToShotline));
                robot.swerveToTarget();
                if (!robot.swerveInProgress()) {
                    advanceState();
                }
                break;
            case LAUNCH_PARTICLES:
                stateDesc = "Ready to Launch";
                switch (subState) {
                    case 0:
                        subStateDesc = "Load Ring Left";
                        robot.canister.loadRing();
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Set Left Aim Timer";
                        leftPowershotAimTimer.set(POWERSHOT_AIM_DELAY);
                        subState++;
                        break;
                    case 2:
                        subStateDesc = "Aim Left";
                        if (!leftPowershotAimTimer.isExpired()) {
                            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(powershot1, robot.getCoordinate()), false);
                        }
                        subState++;
                        break;
                    case 3:
                        subStateDesc = "Shoot Left Powershot";
                        robot.outtake.launchToTarget(Outtake.GoalPositions.LEFT_POWER_SHOT);
                        subState++;
                        break;
                    case 4:
                        subStateDesc = "Load Ring Center";
                        robot.canister.loadRing();
                        subState++;
                        break;
                    case 5:
                        subStateDesc = "Aim Center Timer Set";
                        centerPowershotAimTimer.set(POWERSHOT_AIM_DELAY);
                        subState++;
                        break;
                    case 6:
                        subStateDesc = "Aim Center";
                        if (!centerPowershotAimTimer.isExpired()) {
                            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(powershot2, robot.getCoordinate()), false);
                        }
                        subState++;
                        break;
                    case 7:
                        subStateDesc = "Shoot Center Powershot";
                        robot.outtake.launchToTarget(Outtake.GoalPositions.CENTER_POWER_SHOT);
                        subState++;
                        break;
                    case 8:
                        subStateDesc = "Load Ring Right Powershot";
                        robot.canister.loadRing();
                        subState++;
                        break;
                    case 9:
                        subStateDesc = "Set Aim Powershot Right Timer ";
                        rightPowershotAimTimer.set(POWERSHOT_AIM_DELAY);
                        subState++;
                        break;
                    case 10:
                        subStateDesc = "Aim Right Powershot";
                        if (!rightPowershotAimTimer.isExpired()) {
                            robot.rotateToTarget(robot.outtake.calculateLaunchHeading(powershot3, robot.getCoordinate()), false);
                        }
                        subState++;
                        break;
                    case 11:
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
                        subStateDesc = "Move to Wobble Box";
                        if (wobblePosition == 0) {
                            robot.updatePath(AutoSwervePositions.generateAutoPaths(AutoSwervePositions.shotLineToWobbleOne));
                        } else if (wobblePosition == 1) {
                            robot.updatePath(AutoSwervePositions.generateAutoPaths(AutoSwervePositions.shotlineToWobbleTwo));
                        } else {
                            robot.updatePath(AutoSwervePositions.generateAutoPaths(AutoSwervePositions.shotlineToWobbleThree));
                        }
                        robot.swerveToTarget();
                        if (!robot.swerveInProgress()) {
                            subState++;
                        }
                        break;
                    case 1:
                        subStateDesc = "Set Wobble Put Down Timer";
                        putDownWobble.set(PUT_DOWN_WOBBLE_DELAY);
                    case 2:
                        subStateDesc = "Lower Arm and Release";
                        if (!putDownWobble.isExpired()) {
                            robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.OUT);
                            robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                        }
                        subState++;
                        break;
                    case 3:
                        subStateDesc = "Set Wobble Fold Timer";
                        wobbleFoldTimer.set(WOBBLE_FOLD_DELAY);
                        subState++;
                        break;
                    case 4:
                        subStateDesc = "Fold Wobble";
                        if (!wobbleFoldTimer.isExpired()){
                            robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.FOLDED);
                            robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                            robot.wobble.setLinearSlidePosition(Wobble.LinearSlidePositions.DOWN);
                        }
                        subState++;
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
                if (wobblePosition == 0) {
                    robot.updatePath(AutoSwervePositions.generateAutoPaths(AutoSwervePositions.wobbleOneToParkline));
                } else if (wobblePosition == 1) {
                    robot.updatePath(AutoSwervePositions.generateAutoPaths(AutoSwervePositions.wobbleTwoToParkline));
                } else {
                    robot.updatePath(AutoSwervePositions.generateAutoPaths(AutoSwervePositions.wobbleThreeToParkline));
                }
                robot.swerveToTarget();
                // make swerve to target
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
                if (!robot.swerveInProgress()) {
                    advanceState();
                }
                break;
            case END:
                stateDesc = "Ending Auto";
                switch (subState){
                    case 0:
                        subStateDesc = "Set Reset Dropdown Timer";
                        resetDropdownTimer.set(RESET_DROPDOWN_DELAY);
                    case 1:
                        subStateDesc = "Reset Dropdown";
                        while (!resetDropdownTimer.isExpired()) {
                            robot.intake.setDropdown(Intake.DropdownPositions.UP);
                        }
                        break;
                    default:
                        break;
                }
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

