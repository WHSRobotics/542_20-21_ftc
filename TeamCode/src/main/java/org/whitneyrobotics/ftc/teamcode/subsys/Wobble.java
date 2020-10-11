package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Wobble {
    private Servo hand;
    private DcMotorEx arm;

    private Toggler clawToggler = new Toggler(2);

    private Toggler armToggler = new Toggler(4);

    public Wobble(HardwareMap wobbleMap) {
        hand = wobbleMap.servo.get("clawServo");
        arm = (DcMotorEx) wobbleMap.dcMotor.get("armMotor");
    }


    public enum ClawPositions {
        OPEN, CLOSE
    }

    public enum ArmPositions {
        FOLDED, OVER, UP, DOWN
    }

    public double[] CLAW_POSITIONS = {0, 90}; // rest, push
    public final double CLAW_OPEN = CLAW_POSITIONS[ClawPositions.OPEN.ordinal()];
    public final double CLAW_CLOSE = CLAW_POSITIONS[ClawPositions.CLOSE.ordinal()];

    public int[] ARM_POSITIONS = {0, 1, 2, 3}; //folded, down, up, over in order change position once motor information and tick information is released
    public final int ARM_FOLDED = ARM_POSITIONS[ArmPositions.FOLDED.ordinal()];
    public final int ARM_DOWN = ARM_POSITIONS[ArmPositions.DOWN.ordinal()];
    public final int ARM_UP = ARM_POSITIONS[ArmPositions.UP.ordinal()];
    public final int ARM_OVER = ARM_POSITIONS[ArmPositions.OVER.ordinal()];

    public String clawStateDescription;
    public void operateClaw(boolean gamepadInput1) {
        clawToggler.changeState(gamepadInput1);
        if (clawToggler.currentState() == 0) {
            hand.setPosition(CLAW_OPEN);
            clawStateDescription = "Open";
        } else {
            hand.setPosition(CLAW_CLOSE);
            clawStateDescription = "Close";
        }
    }

    int armState;
    public String armStateDescription;
    public void operateArm(boolean gamepadInput1) {
        armToggler.changeState(gamepadInput1);
        armState = armToggler.currentState();
        switch (armState) {
            case 1: //ARM_DOWN
                arm.setTargetPosition(ARM_DOWN);
                armStateDescription = "Arm Down";
                break;
            case 2: //ARM_UP
                arm.setTargetPosition(ARM_UP);
                armStateDescription = "Arm Up";
                break;
            case 3: //ARM_OVER
                arm.setTargetPosition(ARM_OVER);
                armStateDescription = "Arm Over Wall";
            default: //ARM_FOLDED
                arm.setTargetPosition(ARM_FOLDED);
                armStateDescription = "Arm Folded in Robot";
        }
    }
}