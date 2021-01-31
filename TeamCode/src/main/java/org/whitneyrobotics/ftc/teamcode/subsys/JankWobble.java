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

    private double WOBBLE_POWER = 0.25;

    //REST, RELEASE, GRAB
    public int[] armPositions = {-120,0,250};

    //OPEN, CLOSE
    public double[] clawPositions = {0.45, 0.75};

    private Toggler jankOperateToggler = new Toggler(2);

    private Toggler wobbleToggler = new Toggler(6);
    SimpleTimer releaseTimer = new SimpleTimer();
    private final double RELEASE_DELAY = 3.0;
    boolean wobbleReleased = false;
    int substate = 0;

    public JankWobble(HardwareMap wobbleMap) {
        claw = wobbleMap.servo.get("clawServo");
        wobbleMotor = wobbleMap.dcMotor.get("wobbleMotor");
        wobbleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setClawPosition(ClawPosition position){
        claw.setPosition(clawPositions[position.ordinal()]);
    }

    public void setArmPosition(WobblePosition position){
        wobbleMotor.setTargetPosition(armPositions[position.ordinal()]);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleMotor.setPower(WOBBLE_POWER);
    }

    public void operate(boolean gamepadInput1, boolean gamepadInput2){
        wobbleToggler.changeState(gamepadInput1,gamepadInput2);

        if(wobbleToggler.currentState() == 0){
            setClawPosition(ClawPosition.CLOSED);
            setArmPosition(WobblePosition.REST);
        }
        else if(wobbleToggler.currentState() == 1){
            setClawPosition(ClawPosition.CLOSED);
            setArmPosition(WobblePosition.GRAB);
        }
        else if(wobbleToggler.currentState() == 2){
            setClawPosition(ClawPosition.OPEN);
            setArmPosition(WobblePosition.GRAB);
        }
        else if(wobbleToggler.currentState() == 3){
            setClawPosition(ClawPosition.CLOSED);
            setArmPosition(WobblePosition.GRAB);
        }
        else if(wobbleToggler.currentState() == 4){
            WOBBLE_POWER *= 2;
            setClawPosition(ClawPosition.CLOSED);
            setArmPosition(WobblePosition.RELEASE);
        }
        else if(wobbleToggler.currentState() == 5){
            WOBBLE_POWER *= 1/2;
            setClawPosition(ClawPosition.OPEN);
            setArmPosition(WobblePosition.RELEASE);
        }
    }

    public void releaseWobble(){
        switch(substate){
            case 0:
                releaseTimer.set(RELEASE_DELAY);
                substate++;
                break;
            case 1:
                setArmPosition(WobblePosition.GRAB);
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


    public void jankOperate(boolean gamepadInput1, boolean gamepadInput2, boolean gamepadInput3){
        WOBBLE_POWER = 0.45;
        jankOperateToggler.changeState(gamepadInput3);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if(gamepadInput1){
            wobbleMotor.setPower(WOBBLE_POWER);
        } else if(gamepadInput2){
            wobbleMotor.setPower(-WOBBLE_POWER);
        }else{
            wobbleMotor.setPower(0);
        }

        if(jankOperateToggler.currentState() == 0){
            setClawPosition(ClawPosition.CLOSED);
        }else{
            setClawPosition(ClawPosition.OPEN);
        }
    }
}
