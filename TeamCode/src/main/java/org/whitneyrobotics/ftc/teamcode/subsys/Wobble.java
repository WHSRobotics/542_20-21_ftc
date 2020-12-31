package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Wobble {
    private Servo claw;
    private Servo trapDoor;
    private DcMotor linearSlide;

    private Toggler clawToggler = new Toggler(2);

    public Wobble(HardwareMap wobbleMap) {
        claw = wobbleMap.servo.get("clawServo");
        linearSlide = (DcMotor) wobbleMap.dcMotor.get("armMotor");
    }

    public enum ClawPositions {
        OPEN, CLOSE
    }

    public int[] ARM_POSITIONS = {0, 1, 2, 3}; //placeholder, these are motor tick counts set to represent vertical position on the linear slide

    public double[] CLAW_POSITIONS = {0, 90}; // rest, push

    public String clawStateDescription;
    public void operateClaw(boolean gamepadInput) {
        clawToggler.changeState(gamepadInput);
        if (clawToggler.currentState() == 0) {
            claw.setPosition(CLAW_POSITIONS[OldWobble.ClawPositions.OPEN.ordinal()]);
            clawStateDescription = "Open";
        } else {
            claw.setPosition(CLAW_POSITIONS[OldWobble.ClawPositions.CLOSE.ordinal()]);
            clawStateDescription = "Close";
        }
    }

    public void setClawPosition(Wobble.ClawPositions clawPosition){
        claw.setPosition(CLAW_POSITIONS[clawPosition.ordinal()]);
    }

    public void setLinearSlidePosition(Wobble.ARM_POSITIONS linearSlidePosition){
        linearSlidePosition.setPosition(ARM_POSITIONS[linearSlidePosition()]);
    }



}
