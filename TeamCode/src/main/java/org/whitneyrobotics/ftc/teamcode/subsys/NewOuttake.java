package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.whitneyrobotics.ftc.teamcode.lib.control.ControlConstants;
import org.whitneyrobotics.ftc.teamcode.lib.control.PIDFController;
import org.whitneyrobotics.ftc.teamcode.lib.util.RobotConstants;

public class NewOuttake {

    public DcMotorEx flywheel;
    public Servo flapServo;

    public PIDFController outtakeController;


    public enum GoalPositions{
        LOW_GOAL, MEDIUM_GOAL, HIGH_GOAL, POWERSHOT_TARGET;
    }

    //LOW, Medium, High, Powershot
    public double[] flapServoPositions = {0.0, 0.25, 0.5, 0.75};
    public double[] targetMotorVelocities = {0.0, 0.33, 0.66, 1.0};

    public NewOuttake (HardwareMap outtakeMap){
        flywheel = outtakeMap.get(DcMotorEx.class, "flywheel");
        flapServo = outtakeMap.servo.get("flapServo");
        outtakeController = new PIDFController(RobotConstants.FLYWHEEL_CONSTANTS);
    }

    public void operate(GoalPositions goalPosition){
        operateFlywheel(goalPosition);
        setFlapServoPositions(goalPosition);
    }

    public void setFlapServoPositions(GoalPositions goalPosition){
        flapServo.setPosition(flapServoPositions[goalPosition.ordinal()]);
   }

   public void operateFlywheel(GoalPositions goalPosition){
        double currentVelocity = flywheel.getVelocity();
        double targetVelocity = targetMotorVelocities[goalPosition.ordinal()];
        double error = targetVelocity - currentVelocity;

        outtakeController.calculate(error, 10, currentVelocity);
        flywheel.setPower(outtakeController.getOutput());
   }

}
