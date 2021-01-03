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

    public Toggler wobbleTog = new Toggler(5);

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
                wobbleDesc = "Folded";
                setArmRotratorPositions(ArmRotatorPositions.FOLDED);
                setClawPosition(ClawPositions.CLOSE);
                setLinearSlidePosition(LinearSlidePositions.DOWN);

            case 1:
                wobbleDesc = "Open";
                setArmRotratorPositions(ArmRotatorPositions.OUT);
                setLinearSlidePosition(LinearSlidePositions.MEDIUM);
                setClawPosition(ClawPositions.OPEN);

            case 2:
                wobbleDesc = "Close";
                setArmRotratorPositions(ArmRotatorPositions.IN);
                setClawPosition(ClawPositions.CLOSE);
                setLinearSlidePosition(LinearSlidePositions.DOWN);

            case 3:
                wobbleDesc = "Take Intake Feed";
                setArmRotratorPositions(ArmRotatorPositions.FOLDED);
                setClawPosition(ClawPositions.CLOSE);
                setLinearSlidePosition(LinearSlidePositions.DOWN);

            case 4:
                wobbleDesc = "Lift Over Wall";
                int liftState = 0;
                switch (liftState) {
                    case 0:
                        delayTimer.set(1000);
                        liftState++;
                        break;
                    case 1:
                        if (!delayTimer.isExpired()) {
                            setLinearSlidePosition(LinearSlidePositions.UP);
                            liftState++;
                        }
                        break;
                    case 2:
                        delayTimer.set(750);
                        liftState++;
                        break;
                    case 3:
                        if (!delayTimer.isExpired()){
                            setArmRotratorPositions(ArmRotatorPositions.OUT);
                            liftState++;
                        }
                        break;
                    case 4:
                        delayTimer.set(500);
                        liftState++;
                        break;
                    case 5:
                        if (!delayTimer.isExpired()) {
                            setClawPosition(ClawPositions.OPEN);
                        }
                        break;
                    default:
                        break;
                }

            default:
                break;
        }
    }

}
