package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwerveToTarget;
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

    Coordinate[][] startingOpCoordinateArray = new Coordinate [][];
    // Position[][] autoOpScanningDistanceArray = new Position[];
    Position[][] autoOpPowerShotLineArray = new Position[200][-100];
    Position[][] autoOPWobblePositionOne = new Position[][];
    Position[][] autoOPWobblePositionTwo = new Position[][];
    Position[][] autoOpWobblePositionThree = new Position[][];
    Position[][] autoOpLaunchLine = new Position[][];

    SwerveToTarget driveToShotLine;
    SwerveToTarget driveToWobblePositionOne;
    SwerveToTarget driveToWobblePositionTwo;
    SwerveToTarget driveToWobblePositionThree;
    SwerveToTarget driveToStartingPointFromWobbleOne;
    SwerveToTarget driveToStartingPointFromWobbleTwo;
    SwerveToTarget driveToStartingPointFromWobbleThree;
    private void instantiateSwerveToTargets() {
        Position[] startToFoundationSwervePositions = {startingCoordinateArray[STARTING_ALLIANCE], foundationStartingPositionArray[STARTING_ALLIANCE]};
        Position[] foundationToWallSwervePositions = {foundationStartingPositionArray[STARTING_ALLIANCE], foundationMovedPositionArray[STARTING_ALLIANCE]};
        Position[] wallToMidpointPositions = {startingCoordinateArray[STARTING_ALLIANCE], slideOutFromFoundationMidpointArray[STARTING_ALLIANCE]};
        Position[] midpointToSkystonePositions = {skybridgePositionArray[STARTING_ALLIANCE][SKYBRIDGE_CROSSING_POSITION], skystonePositionArray[STARTING_ALLIANCE][2]};
        Position[] movedFoundationToParkingSwervePositions = {pullFoundationMidpointArray[STARTING_ALLIANCE], parkingMidpointArray[STARTING_ALLIANCE], parkingPositions[STARTING_ALLIANCE]};
        Position[] wallToParkingSwervePositions = {startingCoordinateArray[STARTING_ALLIANCE], skybridgePositionArray[STARTING_ALLIANCE][SKYBRIDGE_CROSSING_POSITION]};
        Position[] foundationMidpointToFoundationMidpointTwoPositions = {foundationMovedPositionArray[STARTING_ALLIANCE], pullFoundationMidpointArray[STARTING_ALLIANCE]};
        Position[] startToParkingSwervePositions = {pullFoundationMidpointArray[STARTING_ALLIANCE], parkingPositions[STARTING_ALLIANCE]};
    }

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

        public void defineStateEnabledStatus(){
            stateEnabled[INIT] = true;
            stateEnabled[DRIVE_TO_LAUNCH_POINT] = true;
            stateEnabled[LAUNCH_PARTICLES_AND_SCAN_STACK] = true;
            stateEnabled[DROP_OFF_WOBBLE_GOAL] = true;
            stateEnabled[PARK_ON_LAUNCH_LINE] = true;
            stateEnabled[END] = true;

        }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
