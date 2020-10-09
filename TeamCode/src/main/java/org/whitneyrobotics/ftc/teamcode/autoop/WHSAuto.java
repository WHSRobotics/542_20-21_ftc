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

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
