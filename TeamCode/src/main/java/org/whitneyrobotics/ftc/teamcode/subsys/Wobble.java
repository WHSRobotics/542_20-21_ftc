package org.whitneyrobotics.ftc.teamcode.subsys;

import android.widget.Switch;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Wobble {
    public Servo armRotator;
    public Servo trapDoor;
    public DcMotor linearSlide;

    public Toggler wobbleTog = new Toggler(7);

    public SimpleTimer delayTimer = new SimpleTimer();

    public Wobble(HardwareMap wobbleMap) {
        armRotator = wobbleMap.servo.get("clawServo");
        trapDoor = wobbleMap.servo.get("trapDoorServo");
        linearSlide = (DcMotor) wobbleMap.dcMotor.get("armMotor");
    }

    public enum ArmRotatorPositions{
        FOLDED, IN, OUT
    }

    public enum ClawPositions {
        OPEN, CLOSE
    }
    public  enum LinearSlidePositions{
        DOWN, MEDIUM, UP
    }

    public double[] ARM_ROTATOR_POSITIONS = {0, 1, 2}; //folded, in , out; test
    public double[] CLAW_POSITIONS = {0, 0.75}; // open, close;test
    public int[] LINEAR_SLIDE_POSITIONS = {0, 1, 2}; //down, medium, up; test

    /*public String clawStateDescription;
    public void operateClaw(boolean gamepadInput) {
        clawToggler.changeState(gamepadInput);
        if (clawToggler.currentState() == 0) {
            armRotator.setPosition(CLAW_POSITIONS[OldWobble.ClawPositions.OPEN.ordinal()]);
            clawStateDescription = "Open";
        } else {
            armRotator.setPosition(CLAW_POSITIONS[OldWobble.ClawPositions.CLOSE.ordinal()]);
            clawStateDescription = "Close";
        }
    }*/


    public void setArmRotratorPositions(ArmRotatorPositions armRotatorPosition){
        armRotator.setPosition(ARM_ROTATOR_POSITIONS[armRotatorPosition.ordinal()]);
    }

    public void setClawPosition(ClawPositions clawPosition){
        trapDoor.setPosition(CLAW_POSITIONS[clawPosition.ordinal()]);
    }

    public void setLinearSlidePosition(LinearSlidePositions linearSlidePosition){
        linearSlide.setTargetPosition(LINEAR_SLIDE_POSITIONS[linearSlidePosition.ordinal()]);
    }

    public String wobbleDesc;

    public void operateWobble(boolean stateFwd, boolean stateBkwd){
        wobbleTog.changeState(stateFwd, stateBkwd);
        switch (wobbleTog.currentState()){
            case 0:
                wobbleDesc = "Folded/Take Intake Feed";
                setArmRotratorPositions(ArmRotatorPositions.FOLDED);
                setClawPosition(ClawPositions.CLOSE);
                setLinearSlidePosition(LinearSlidePositions.DOWN);

            case 1:
                wobbleDesc = "Take Stuff";
                setArmRotratorPositions(ArmRotatorPositions.OUT);
                setClawPosition(ClawPositions.OPEN);
                setLinearSlidePosition(LinearSlidePositions.MEDIUM);

            case 2:
                wobbleDesc = "Carry Stuff";
                setArmRotratorPositions(ArmRotatorPositions.IN);
                setClawPosition(ClawPositions.CLOSE);
                setLinearSlidePosition(LinearSlidePositions.MEDIUM);

            case 3:
                wobbleDesc = "Are you sure you want to lift over wall?";
            case 4:
                wobbleDesc = "Raise to Wall Level";
                setLinearSlidePosition(LinearSlidePositions.UP);

            case 5:
                wobbleDesc = "Extend Out Over Wall";
                setArmRotratorPositions(ArmRotatorPositions.OUT);

            case 6:
                wobbleDesc = "Release";
                setClawPosition(ClawPositions.OPEN);
            default:
                break;
        }
    }

}
