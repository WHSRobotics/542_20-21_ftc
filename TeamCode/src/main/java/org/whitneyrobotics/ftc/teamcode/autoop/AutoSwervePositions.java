package org.whitneyrobotics.ftc.teamcode.autoop;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;

import java.util.ArrayList;

public class AutoSwervePositions {
    // declare positions here put spaces between each path you are generating for and label which path it is for
    // test path
    public static Position p1Test = new Position(1,2);
    public static Position p2Test = new Position(3,4);
    //Start to Shotline
    public static Position p1startToShotline = new Position(-1500, -600);

    public static ArrayList<Position> testPath = new ArrayList<Position>();

    public static void initArrayList(){
        //path 1
        testPath.add(p1Test);
        testPath.add(p2Test);
    }

    public static ArrayList<Position> getPath(ArrayList<Position> pathArray){
        initArrayList();
        return pathArray;
    }

}
