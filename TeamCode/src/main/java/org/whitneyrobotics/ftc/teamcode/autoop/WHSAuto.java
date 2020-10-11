package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwerveToTarget;
import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

public class WHSAuto extends OpMode {
    WHSRobotImpl robot;

    static final int RED = 0;
    static final int BLUE = 1;
    static final int INSIDE = 0;
    static final int OUTSIDE = 1;

    static final int STARTING_POSITION = 0;
    public static final int STARTING_ALLIANCE = RED;
    static final double STARTING_COORDINATE_X = 1200;
    static final double STARTING_COORDINATE_Y = -1800;
    static final boolean PARTNER_MOVED_WOBBLE = false;

    static final double LEFT_MAX = 80;
    static final double CENTER_MAX = 165;

    static final int LEFT = 0;
    static final int CENTER = 1;
    static final int RIGHT = 2;

    int powerShotPosition = CENTER;

    Coordinate[] startingOpCoordinateArray = new Coordinate [1];
    Position[][] autoOpScanningDistanceArray = new Position[10][11];
    Position[][] autoOpPowerShotLineArray = new Position[200][-100];
    Position[][] autoOPWobblePositionOne = new Position[2][3];
    Position[][] autoOPWobblePositionTwo = new Position[4][5];
    Position[][] autoOpWobblePositionThree = new Position[6][7];
    Position[][] autoOpLaunchLine = new Position[8][9];

    SwerveToTarget driveToShotLineSwerve;
    SwerveToTarget driveToWobblePositionOneSwerve;
    SwerveToTarget driveToWobblePositionTwoSwerve;
    SwerveToTarget driveToWobblePositionThreeSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleOneSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleTwoSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleThreeSwerve;

    private void instantiateSwerveToTargets() {
        Position [][] driveToShotLineSwervePositions = {autoOpScanningDistanceArray[STARTING_ALLIANCE], autoOpPowerShotLineArray[STARTING_ALLIANCE]};
        Position [][] driveToWobblePositionOneSwervePositions = {autoOpPowerShotLineArray[STARTING_ALLIANCE], autoOPWobblePositionOne[STARTING_ALLIANCE]};
        Position [][] driveToWobblePositionTwoSwervePositions = {autoOpPowerShotLineArray[STARTING_ALLIANCE], autoOPWobblePositionTwo[STARTING_ALLIANCE]};
        Position [][] driveToWobblePositionThreeSwervePositions = {autoOpPowerShotLineArray[STARTING_ALLIANCE], autoOpWobblePositionThree[STARTING_ALLIANCE]};
        Position [][] driveToLaunchLineFromWobbleOneSwervePositions = {autoOPWobblePositionOne[STARTING_ALLIANCE], autoOpLaunchLine[STARTING_ALLIANCE]};
        Position [][] driveToLaunchLineFromWobbleTwoSwervePositions = {autoOPWobblePositionTwo[STARTING_ALLIANCE], autoOpLaunchLine[STARTING_ALLIANCE]};
        Position [][] driveToLaunchLineFromWobbleThreeSwervePositions = {autoOpWobblePositionThree[STARTING_ALLIANCE], autoOpLaunchLine[STARTING_ALLIANCE]};
    }

  //insert Swerve to Target Here
  final double STRAFE_TO_RING_LAUNCH_POWER = 0.7542;

    //State definitions
    static final int INIT = 0;
    static final int DRIVE_TO_LAUNCH_POINT = 1;
    static final int LAUNCH_PARTICLES_AND_SCAN_STACK = 2;
    static final int DROP_OFF_WOBBLE_GOAL = 3;
    static final int PARK_ON_LAUNCH_LINE = 4;
    static final int END = 5;

    static final int NUMBER_OF_STATES = 6;

    boolean[] stateEnabled = new boolean[NUMBER_OF_STATES];

    int state = INIT;
    int subState = 0;

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
            stateEnabled[DRIVE_TO_LAUNCH_POINT] = true;
            stateEnabled[LAUNCH_PARTICLES_AND_SCAN_STACK] = true;
            stateEnabled[DROP_OFF_WOBBLE_GOAL] = true;
            stateEnabled[PARK_ON_LAUNCH_LINE] = true;
            stateEnabled[END] = true;
        }

        //timers

        SimpleTimer scannerTimer = new SimpleTimer();
        SimpleTimer driveToLaunchTimer = new SimpleTimer();
        SimpleTimer launchTimer = new SimpleTimer();
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

        startingOpCoordinateArray[RED] = new Coordinate(STARTING_COORDINATE_X, -1571, 90);

        //all coordinates here are placeholders, change later
        autoOpScanningDistanceArray[RED][0] = new Position(1, -2);
        autoOpPowerShotLineArray[RED][0] =new Position(3, -4);
        autoOPWobblePositionOne[RED][0] = new Position(5,-6);
        autoOPWobblePositionTwo[RED][0] = new Position(7,-8);
        autoOpWobblePositionThree[RED][0] = new Position(9, -10);
        autoOpLaunchLine[RED][0] = new Position(11,-12);

    }

    @Override
    public void loop() {

    }
}
