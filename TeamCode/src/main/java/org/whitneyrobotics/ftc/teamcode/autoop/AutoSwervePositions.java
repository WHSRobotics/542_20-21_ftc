package org.whitneyrobotics.ftc.teamcode.autoop;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;

import java.util.ArrayList;

public class AutoSwervePositions {

    // swerve Lookahed Distances
    public static double startToShotlineLookaheadDist = 350; // in mm
    // swerve spacing
    public static double startToShotlineWaypointSpacing = 80; // in mm
    // swerve weight smooth
    public static double startToShotlineWeightSmooth = 0.5;
    // swerve turn speed
    public static double startToShotlineTurnSpeed = 750;
    // swerve max velocity
    public static double startToShotlineMaxVelocity = 3; // 1 - 5
    // declare positions here put spaces between each path you are generating for and label which path it is for
    // test path
    public static Position p1Test = new Position(1,2);
    public static Position p2Test = new Position(3,4);
    //Start to Shotline
    public static Position p1StartToShotline = new Position(-1500, -600);
    public static Position p2StartToShotLine = new Position(-600, 0);
    public static Position p3StartToShotLine = new Position(0, -300);

    //make  ArrayLists here Call these in getPath
    public static ArrayList<Position> testPath = new ArrayList<Position>();
    public static ArrayList <Position> startToShotlinePath = new ArrayList<Position>();


    public static void initArrayList(){
        //test path
        testPath.add(p1Test);
        testPath.add(p2Test);
        //Start to Shotline
        startToShotlinePath.add(p1StartToShotline);
        startToShotlinePath.add(p2StartToShotLine);
        startToShotlinePath.add(p3StartToShotLine);

    }

    public static ArrayList<Position> getPath(ArrayList<Position> pathArray){
        initArrayList();
        return pathArray;
    }

}
