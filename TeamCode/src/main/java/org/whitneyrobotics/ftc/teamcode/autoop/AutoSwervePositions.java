package org.whitneyrobotics.ftc.teamcode.autoop;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;

import java.util.ArrayList;

public class AutoSwervePositions {

    // declare positions here put spaces between each path you are generating for and label which path it is for
    // test path
    public static Position p1Test = new Position(1,2);
    public static Position p2Test = new Position(3,4);
    //Start to Shotline
    public static Position p1StartToShotline = new Position(-1500, -600);
    public static Position p2StartToShotLine = new Position(-600, 0);
    public static Position p3StartToShotLine = new Position(0, -300);
    //shotlineToWobble1
    public static Position p1ShotlineToWobble1 = new Position(0, -300);
    public static Position p2ShotlineToWobble1 = new Position(300, -1500);
    //shotlineToWobble2
    public static Position p1ShotlineToWobble2 = new Position(0, -300);
    public static Position p2ShotlineToWobble2 = new Position(900, -900);
    //ShotlineToWobble3
    public static Position p1ShotlineToWobble3 = new Position(0, -300);
    public static Position p2ShotlineToWobble3 = new Position(1500, -1500);
    //Wobble1ToParking
    public static Position p1Wobble1ToParking = new Position(300, -1500);
    public static Position p2Wobble1ToParking = new Position(300, -1500);
    //Wobble2ToParking
    public static Position p1Wobble2ToParking = new Position(900, -900);
   public static Position p2Wobble2ToParking = new Position(300, -900);
   //Wobble3ToParking
   public static Position p1Wobble3ToParking = new Position(1500, -1500);
   public static Position p2Wobble3ToParking = new Position(300, -1500);


    // swerve Lookahed Distances
    public static double startToShotlineLookaheadDist = 350; // in mm
    public static double shotlineToWobble1LookaheadDist = 350; //in mm
    public static double shotlineToWobble2LookahadDist = 350;
    public static double shotlineToWobble3LookaheadDist = 350;
    public static double wobble1ToParkLookaheadDist = 350;
    public static double wobble2ToParkLookaheadDist = 350;
    public static double wobble3ToParkLookaheadDist = 350;
    // swerve spacing
    public static double startToShotlineWaypointSpacing = 80; // in mm
    public static double shotlineToWobble1Spacing = 80;
    public static double shotlineToWobble2Spacing = 80;
    public static double shotlineToWobble3Spacing = 80;
    public static double wobble1ToParkSpacing = 80;
    public static double wobble2ToParkSpacing = 80;
    public static double wobble3ToParkSpacing = 80;

    // swerve weight smooth
    public static double startToShotlineWeightSmooth = 0.5;
    public static double shotlineToWobble1WeightSmooth = 0.5;
    public static double shotlineToWobble2WeightSmooth = 0.5;
    public static double shotlineToWobble3WeightSmooth = 0.5;
    public static double wobble1ToParkWeightSmooth = 0.5;
    public static double wobble2ToParkWeightSmooth = 0.5;
    public static double wobble3ToParkWeightSmooth = 0.5;
    // swerve turn speed
    public static double startToShotlineTurnSpeed = 750;

    // swerve max velocity
    public static double startToShotlineMaxVelocity = 3; // 1 - 5

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
