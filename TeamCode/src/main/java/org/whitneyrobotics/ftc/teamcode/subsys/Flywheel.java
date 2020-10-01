package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Flywheel {
    public final double POWER_SHOT_TARGET_HEIGHT = 584.2;
    public final double MID_GOAL_HEIGHT = 685.8;
    public final double HIGH_TARGET_HEIGHT = 901.7;
    public DcMotorEx launcher;
    public enum LaunchTargets{
        POWERSHOT1, POWERSHOT2, POWERSHOT3, BINS
    }
    Position powershot1 = new Position(0,0);
    Position powershot2 = new Position(0,0);
    Position powershot3 = new Position(0,0);
    Position bins = new Position(0,0);
    public Position[] Target_Positions = {powershot1, powershot2, powershot3, highbin, midbin};
    public final Position Pow1 = Target_Positions[LaunchTargets.POWERSHOT1.ordinal()];
    public final Position Pow2 = Target_Positions[LaunchTargets.POWERSHOT2.ordinal()];
    public final Position Pow3 = Target_Positions[LaunchTargets.POWERSHOT3.ordinal()];
    public final Position Bin = Target_Positions[LaunchTargets.BINS.ordinal()];
    public final double FLYWHEEL_POWER = 0.5;
    public Flywheel(HardwareMap flyMap){
        launcher = flyMap.get(DcMotorEx.class, "FlyWheel");
    }
    Toggler flyWheelTog = new Toggler(2);
    public int launchState;
    public void calculateLaunchHeading(Position target, Coordinate robotPos){

    }
    public void operate(boolean togInc, boolean togDec){
        flyWheelTog.changeState(togInc, togDec);
        launchState = flyWheelTog.currentState();
        switch (launchState){
            case 0:
                launcher.setPower(0);
                break;
            case 1:
                launcher.setPower(FLYWHEEL_POWER);
                break;
        }
    }
    public void On (){
        launcher.setPower(0);
    }
    public  void Off (){
        launcher.setPower(FLYWHEEL_POWER);
    }
}
