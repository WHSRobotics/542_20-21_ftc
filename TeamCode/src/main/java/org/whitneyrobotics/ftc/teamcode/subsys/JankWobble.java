package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class JankWobble {
    public DcMotor wobbleMotor;
    public Servo claw;

    public enum WobblePosition{
        REST, RELEASE, GRAB
    }

    public enum ClawPosition{
        OPEN, CLOSED
    }

    private final double WOBBLE_POWER = 0.5;

    //REST, RELEASE, GRAB
    public int[] armPositions = {0,200,500};

    //OPEN, CLOSE
    public double[] clawPositions = {0.5, 0.75};

    private Toggler wobbleToggler = new Toggler(6);
    SimpleTimer releaseTimer;
    private final double RELEASE_DELAY = 3.0;
    boolean wobbleReleased = false;

    public JankWobble(HardwareMap wobbleMap) {
        claw = wobbleMap.servo.get("clawServo");
        wobbleMotor = wobbleMap.dcMotor.get("wobbleMotor");
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setClawPosition(ClawPosition position){
        claw.setPosition(clawPositions[position.ordinal()]);
    }

    public void setArmPosition(WobblePosition position){
        wobbleMotor.setTargetPosition(armPositions[position.ordinal()]);
        wobbleMotor.setPower(WOBBLE_POWER);
    }

    public void operate(boolean gamepadInput1, boolean gamepadInput2){
        wobbleToggler.changeState(gamepadInput1,gamepadInput2);

        if(wobbleToggler.currentState() == 0){
            setClawPosition(ClawPosition.OPEN);
            setArmPosition(WobblePosition.REST);
        }
        else if(wobbleToggler.currentState() == 1){
            setClawPosition(ClawPosition.OPEN);
            setArmPosition(WobblePosition.GRAB);
        }
        else if(wobbleToggler.currentState() == 2){
            setClawPosition(ClawPosition.CLOSED);
            setArmPosition(WobblePosition.GRAB);
        }
        else if(wobbleToggler.currentState() == 3){
            setClawPosition(ClawPosition.CLOSED);
            setArmPosition(WobblePosition.REST);
        }
        else if(wobbleToggler.currentState() == 4){
            setClawPosition(ClawPosition.CLOSED);
            setArmPosition(WobblePosition.RELEASE);
        }
        else if(wobbleToggler.currentState() == 5){
            setClawPosition(ClawPosition.OPEN);
            setArmPosition(WobblePosition.RELEASE);
        }
    }

    public void releaseWobble(){
        int substate = 0;
        switch(substate){
            case 0:
                releaseTimer.set(RELEASE_DELAY);
                substate++;
                break;
            case 1:
                setArmPosition(WobblePosition.RELEASE);
                if(releaseTimer.isExpired()){
                    substate++;
                }
                break;
            case 2:
                setClawPosition(ClawPosition.OPEN);
                wobbleReleased = true;
                break;
            default:
                break;
        }

    }

    public boolean wobbleReleased(){
        return wobbleReleased;
    }
}
