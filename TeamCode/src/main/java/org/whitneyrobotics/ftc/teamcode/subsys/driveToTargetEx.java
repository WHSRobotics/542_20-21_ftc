package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.autoop.AutoSwervePositions;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;

import java.util.ArrayList;

public class driveToTargetEx extends OpMode {
    WHSRobotImpl robot;
    Coordinate styarting;
    Position pos = new Position(8, 800);
    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        styarting = new Coordinate(0, 0, 0);

    }

    @Override
    public void loop() {
        robot.setInitialCoordinate(styarting);
        robot.driveToTarget(pos, false);
        ArrayList<Position> bob = AutoSwervePositions.getPath(AutoSwervePositions.testPath);
    }
}
