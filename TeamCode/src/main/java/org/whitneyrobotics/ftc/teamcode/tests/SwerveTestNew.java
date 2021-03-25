package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.FollowerConstants;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.PathGenerator;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwervePath;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwervePathGenerationConstants;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SwerveTestNew extends OpMode {
    WHSRobotImpl robot;
    SwervePath path;
    Coordinate startingCoordinate = new Coordinate(2,3,0);
    Position p1 = new Position(1,2);
    Position p2 = new Position(3, 4);
    Position p3 = new Position(4, 5);

    ArrayList<Position> posArray = new ArrayList<Position>();
    FollowerConstants followerConstants = new FollowerConstants(12,true);
    SwervePathGenerationConstants pathGenerationConstants = new SwervePathGenerationConstants(12,23,23,230);

    public void init(){
        posArray.add(p1);
        posArray.add(p2);
        posArray.add(p3);

        path = PathGenerator.generateSwervePath(posArray, followerConstants, pathGenerationConstants);
    }
    public void loop(){
        robot.updatePath(path);
        robot.swerveToTarget();

        if(!robot.swerveInProgress()){
            //move on code
        }
    }


}
