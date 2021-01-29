package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotJank;

public class WHSJankAuto extends OpMode {
    WHSRobotJank robot;

    // Starting color and inside/outside array values
    static final int RED = 0;
    int STARTING_COORDINATE_X =325;
    Coordinate startingCoordinate;
    Position parkingPosition;
    int state = 0;
    String stateDesc;


    @Override
    public void init() {
        startingCoordinate = new Coordinate(STARTING_COORDINATE_X,-1500, 0);
        parkingPosition = new Position(STARTING_COORDINATE_X,0);
    }

    @Override
    public void loop() {
        switch(state){
            case 0:
                stateDesc = "Init";
                robot.setInitialCoordinate(startingCoordinate);
                state++;
                break;
            case 1:
                stateDesc = "Driving To Launch Line";
                robot.driveToTarget(parkingPosition,false);
                if(!robot.driveToTargetInProgress()){
                    state++;
                }
                break;
            case 2:
                stateDesc = "Rotating To Drop Wobble";
                robot.rotateToTarget(45,false);
                if(!robot.rotateToTargetInProgress()){
                    state++;
                }
                break;
            case 3:
                stateDesc = "Release Wobble";
                robot.wobble.releaseWobble();
                if(robot.wobble.wobbleReleased()){
                    state++;
                }
            case 4:
                stateDesc = "End";
                break;
            default:
                break;  
        }
    }
}
