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
    public static double shotlineToWobble1TurnSpeed = 750;
    public static double shotlineToWobble2TurnSpeed = 750;
    public static double shotlineToWobble3TurnSpeed = 750;
    public static double wobble1ToParkTurnSpeed = 750;
    public static double wobble2ToParkTurnSpeed = 750;
    public static double wobble3ToParkTurnSpeed = 750;

    // swerve max velocity
    public static double startToShotlineMaxVelocity = 3; // 1 - 5
    public static double shotlineToWobble1MaxVelocity = 3;
    public static double shotlineToWobble2MaxVelocity = 3;
    public static double shotlineToWobble3MaxVelocity = 3;
    public static double wobble1ToParkMaxVelocity = 3;
    public static double wobble2ToParkMaxVelocity = 3;
    public static double wobble3ToParkMaxVelocity = 3;

    //make  ArrayLists here Call these in getPath
    public static ArrayList<Position> testPath = new ArrayList<Position>();
    public static ArrayList <Position> startToShotlinePath = new ArrayList<Position>();
    public static ArrayList <Position> shotlineToWobble1Path = new ArrayList<Position>();
    public static ArrayList <Position> shotlineToWobble2Path = new ArrayList<Position>();
    public static ArrayList <Position> shotlineToWobble3Path = new ArrayList<Position>();
    public static ArrayList <Position> wobble1ToParkPath = new ArrayList<Position>();
    public static ArrayList <Position> wobble2ToParkPath = new ArrayList<Position>();
    public static ArrayList <Position> wobble3ToParkPath = new ArrayList<Position>();


    public static void initArrayList(){
        //test path
        testPath.add(p1Test);
        testPath.add(p2Test);
        //Start to Shotline
        startToShotlinePath.add(p1StartToShotline);
        startToShotlinePath.add(p2StartToShotLine);
        startToShotlinePath.add(p3StartToShotLine);
        //Shotline to Wobble N
        //N=1
        shotlineToWobble1Path.add(p1ShotlineToWobble1);
        shotlineToWobble1Path.add(p2ShotlineToWobble1);
        //N=2
        shotlineToWobble2Path.add(p1ShotlineToWobble2);
        shotlineToWobble2Path.add(p2ShotlineToWobble2);
        //N=3
        shotlineToWobble3Path.add(p1ShotlineToWobble3);
        shotlineToWobble3Path.add(p2ShotlineToWobble3);
        //Wobble N to Park
        //N=1
        wobble1ToParkPath.add(p1Wobble1ToParking);
        wobble1ToParkPath.add(p2Wobble1ToParking);
        //N=2
        wobble2ToParkPath.add(p1Wobble2ToParking);
        wobble2ToParkPath.add(p2Wobble2ToParking);
        //N=3
        wobble3ToParkPath.add(p1Wobble3ToParking);
        wobble3ToParkPath.add(p2Wobble3ToParking);

    }

    public static ArrayList<Position> getPath(ArrayList<Position> pathArray){
        initArrayList();
        return pathArray;
    }

}
