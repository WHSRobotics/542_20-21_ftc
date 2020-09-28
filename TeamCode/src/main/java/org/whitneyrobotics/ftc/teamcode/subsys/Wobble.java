package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Wobble {
    private Servo hand;
    private Toggler clawToggler = new Toggler(2);

    public Wobble (HardwareMap clawMap){
        hand = clawMap.servo.get("clawServo");
    }

    public enum ClawPositions{
        OPEN, CLOSE
    }

    public double[]CLAW_POSITIONS={0, 90} ; // rest, push
    public final double CLAW_OPEN = CLAW_POSITIONS[ClawPositions.OPEN.ordinal()];
    public final double CLAW_CLOSE = CLAW_POSITIONS[ClawPositions.CLOSE.ordinal()];

    public void operateClaw(boolean gamepadInput1) {
        clawToggler.changeState(gamepadInput1);
        if (clawToggler.currentState() == 0) {
            hand.setPosition(CLAW_OPEN);
        }

        else {
            hand.setPosition(CLAW_CLOSE);
        }
    }
}
