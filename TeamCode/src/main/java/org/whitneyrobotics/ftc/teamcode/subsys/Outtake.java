package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class Outtake {
    public final double POWER_SHOT_TARGET_HEIGHT = 584.2;
    public final double MID_GOAL_HEIGHT = 685.8;
    public final double HIGH_TARGET_HEIGHT = 901.7;
    public DcMotorEx launcher;
    public Servo flap;
    public enum LaunchTargets{
        POWERSHOT1, POWERSHOT2, POWERSHOT3, BINS
    }
    public enum LaunchAngles{
        BIN25, BIN50, BIN75, BIN100
    }
    Position powershot1 = new Position(3.75,28.5); // from right to left fix later
    Position powershot2 = new Position(11.25,28.5);
    Position powershot3 = new Position(18.75,28.5);
    Position bins = new Position(0,0);
    public Position[] Target_Positions = {powershot1, powershot2, powershot3, bins};
    public final Position Pow1 = Target_Positions[LaunchTargets.POWERSHOT1.ordinal()];
    public final Position Pow2 = Target_Positions[LaunchTargets.POWERSHOT2.ordinal()];
    public final Position Pow3 = Target_Positions[LaunchTargets.POWERSHOT3.ordinal()];
    public final Position Bin = Target_Positions[LaunchTargets.BINS.ordinal()];
    public final double FLYWHEEL_POWER = 0.5;
    public Outtake (HardwareMap flyMap) {
        launcher = flyMap.get(DcMotorEx.class, "FlyWheel");
        flap =  hardwareMap.servo.get("Flap");
    }
    public Toggler flyWheelTog = new Toggler(2);
    public Toggler flapTog = new Toggler (4);
    public int[] SERVO_POSITIONS = {25,50,75,100};
    public final int SERVO_ONE = SERVO_POSITIONS[LaunchAngles.BIN25.ordinal()];
    public final int SERVO_TWO = SERVO_POSITIONS[LaunchAngles.BIN50.ordinal()];
    public final int SERVO_THREE = SERVO_POSITIONS[LaunchAngles.BIN75.ordinal()];
    public final int SERVO_BIN = SERVO_POSITIONS[LaunchAngles.BIN100.ordinal()];
    public Position triangle;
    public double leg1;
    public double leg2;
    public double hypotenuse;
    public double targetHeading;
    public boolean above;
    public double headingToTarget;
    public double calculateLaunchHeading(Position target, Coordinate robotPos){
        if (robotPos.getY()>target.getY()){
            above = true;
        }
        else {
            above = false;
        }
        triangle = new Position(robotPos.getX(), target.getY());
        leg1 = Math.abs(triangle.getY()- robotPos.getY());
        leg2 = Math.abs(triangle.getX()-target.getX());
        hypotenuse = Math.sqrt(Math.pow(leg1, 2)+Math.pow(leg2,2));
        targetHeading = Math.asin((leg1*Math.sin(90))/hypotenuse);
        if (above){
            headingToTarget = 180 - targetHeading;
        }
        else{
            headingToTarget = targetHeading;
        }
        return headingToTarget;
    }
    public int launchState;
    public void operate(boolean togInc, boolean togDec,){
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
