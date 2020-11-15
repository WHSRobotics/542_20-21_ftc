package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.control.PIDFController;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.util.Functions;
import org.whitneyrobotics.ftc.teamcode.lib.util.RobotConstants;

public class Outtake {

    public DcMotorEx flywheel;
    public Servo flap;
    public static final double INITIAL_VELOCITY = 7.07;
    public static final double GRAVITY = -9.8;
    public static final double LAUNCHER_HEIGHT = 300;
    public final double FLYWHEEL_POWER = 0.5;
    public Position launchPoint = new Position(300, -285.75);
    public int[] FLAP_POSITIONS = {25,50,75,100};


    public final double POWER_SHOT_TARGET_HEIGHT = 784.225;
    public final double MID_GOAL_HEIGHT = 687.3875;
    public final double HIGH_TARGET_HEIGHT = 901.7; //haven't checked this




    public PIDFController outtakeController;


    public enum GoalPositions{
        BIN_GOALS, POWER_SHOT_TARGET_ONE, POWER_SHOT_TARGET_TWO, POWER_SHOT_TARGET_THREE
    }
    public enum LaunchAngles{
        LOW_BIN, MEDIUM_BIN, HIGH_BIN, P1, P2, P3
    }
    public final Position powershot1 = new Position(1800,-95.25); // from right to left fix later
    public final Position powershot2 = new Position(1800,-285.75);
    public final Position powershot3 = new Position(1800,-476.25);
    public final Position binsMidpoint = new Position(1800,-890.5875);
    public Position[] Target_Positions = {powershot1, powershot2, powershot3, binsMidpoint};
    public final Position Pow1 = Target_Positions[GoalPositions.POWER_SHOT_TARGET_ONE.ordinal()];
    public final Position Pow2 = Target_Positions[GoalPositions.POWER_SHOT_TARGET_TWO.ordinal()];
    public final Position Pow3 = Target_Positions[GoalPositions.POWER_SHOT_TARGET_THREE.ordinal()];
    public final Position Bin = Target_Positions[GoalPositions.BIN_GOALS.ordinal()];

    public double calculateLaunchHeading(Position target, Coordinate robotPos){
        // calculates heading to launch at target
        boolean negativeAngleRequired;
        double headingToTarget;
        if (robotPos.getY()>target.getY()){
            negativeAngleRequired = true;
        }
        else {
            negativeAngleRequired = false;
        }
        Position triangle = new Position(robotPos.getX(), target.getY());
        double robotToTriangle = Math.abs(triangle.getY()- robotPos.getY());
        double targetToTriangle = Math.abs(triangle.getX()- target.getX());
        double targetHeading = Math.atan(targetToTriangle/robotToTriangle);
        if (negativeAngleRequired){
            headingToTarget = -(360 - (270 + targetHeading));
        }
        else{
            headingToTarget = 90 - targetHeading;
        }
        return headingToTarget;
    }
    public double calculateLaunchSetting (double xDistance, double yDistance){
        // calculates angle based on that formula and then determines servo setting
        double angle;
        double launchSetting;
        double repeatedTerm = ((GRAVITY*yDistance*Math.pow(xDistance,2))-(GRAVITY*LAUNCHER_HEIGHT* Math.pow(xDistance, 2)))/Math.pow(INITIAL_VELOCITY, 2);
        double term1 = Math.pow(xDistance, 2) - repeatedTerm; //x^2 - repeated term
        double term2 = Math.pow((repeatedTerm-Math.pow(xDistance, 2)), 2); // first half under the smaller square root
        double term3 = ((Math.pow(GRAVITY, 2)*Math.pow(xDistance, 2))/Math.pow(INITIAL_VELOCITY, 4))*(Math.pow(xDistance, 2)+Math.pow(yDistance, 2)+ Math.pow(LAUNCHER_HEIGHT, 2)-(2*yDistance*LAUNCHER_HEIGHT));// rest of stuff under small rad
        double smallRoot = Math.sqrt(term2-term3);
        double top = term1 + smallRoot; // this could be + or - only testing will tell
        double bottom = 2 * (Math.pow(xDistance, 2) + Math.pow(yDistance, 2) + Math.pow(LAUNCHER_HEIGHT, 2) - (2 * yDistance * LAUNCHER_HEIGHT));
        double inputForArccos = Math.sqrt(top/bottom);
        angle = Math.acos(inputForArccos);
        launchSetting = angle / 90;
        return launchSetting;
    }

    public double calculateDistanceToTarget(Position target, Coordinate robot){
        // calculates distacnce between robot and target
        return Functions.distanceFormula(target.getX(), robot.getX(), target.getX(), robot.getY());
    }

    //Low, Medium, High, Powershot
    public double[] flapServoPositions = {0.0, 0.25, 0.5, 0.75};
    public double[] targetMotorVelocities = {0.0, 0.33, 0.66, 1.0};

    public Outtake(HardwareMap outtakeMap){
        flywheel = outtakeMap.get(DcMotorEx.class, "flywheel");
        flap = outtakeMap.servo.get("flapServo");
        outtakeController = new PIDFController(RobotConstants.FLYWHEEL_CONSTANTS);
    }

    public void operate(GoalPositions goalPosition){
        operateFlywheel(goalPosition);
        setFlapServoPositions(goalPosition);
    }

    public void setFlapServoPositions(GoalPositions goalPosition){
        flap.setPosition(flapServoPositions[goalPosition.ordinal()]);
   }

   public void operateFlywheel(GoalPositions goalPosition){
        double currentVelocity = flywheel.getVelocity();
        double targetVelocity = targetMotorVelocities[goalPosition.ordinal()];
        double error = targetVelocity - currentVelocity;

        outtakeController.calculate(error, 10, currentVelocity);
        flywheel.setPower(outtakeController.getOutput());
   }
    public void setLaunchAngle(OldOuttake.LaunchAngles launchAngle){
        flap.setPosition(FLAP_POSITIONS[launchAngle.ordinal()]);
    }

    public void setLauncherPower(double power){
        flywheel.setPower(power);
    }

}
