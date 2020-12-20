package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.control.PIDFController;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Wobble {

    private Servo claw;
    private DcMotor horizontalMotor;
    private DcMotor verticalMotor;
    //PID
    private PIDFController horizontalMotorPID;
    private PIDFController verticalMotorPID;

    private Toggler clawTog = new Toggler(2);
    private Toggler horizontalPulleyTog = new Toggler(3);
    private Toggler verticalPulleyTog = new Toggler(2);

    public Wobble(HardwareMap wobbleMap) {
        horizontalMotor = wobbleMap.dcMotor.get("horizontalMotor");
        verticalMotor = wobbleMap.dcMotor.get("verticalMotor");
        claw = wobbleMap.servo.get("clawServo");
    }

    public enum ClawPositions {
        OPEN, CLOSE
    }
    public enum HorizontalPositions {
        PLACEHOLDER1, PLACEHOLDER2
    }

    public enum VerticalPositions {
        PLACEHOLDER3, PLACEHOLDER4
    }

    public double[] CLAW_POSITIONS = {0, 90}; // rest, push, placeholders
    public int[] VERTICAL_POSITIONS = {0, 1};//placeholders
    public int[] HORIZONTAL_POSITIONS = {0, 0};//placeholders

    public String clawStateDescription;
    public void operateClaw(boolean gamepadInput) {
        clawTog.changeState(gamepadInput);
        if (clawTog.currentState() == 0) {
            claw.setPosition(CLAW_POSITIONS[ClawPositions.OPEN.ordinal()]);
            clawStateDescription = "Open";
        } else {
            claw.setPosition(CLAW_POSITIONS[ClawPositions.CLOSE.ordinal()]);
            clawStateDescription = "Close";
        }
    }

    public void verticalMovement(boolean gamepadInput){
        verticalPulleyTog.changeState(gamepadInput);
        if (verticalPulleyTog.currentState() == 0 ) {
            verticalMotor.setTargetPosition(VERTICAL_POSITIONS[VerticalPositions.PLACEHOLDER3.ordinal()]);
        }
        else {
            verticalMotor.setTargetPosition(VERTICAL_POSITIONS[VerticalPositions.PLACEHOLDER4.ordinal()]);
        }
    }

    public void horizontalMovement(boolean gamepadInput){
        horizontalPulleyTog.changeState(gamepadInput);
        if (horizontalPulleyTog.currentState() == 1 ) {
            horizontalMotor.setTargetPosition(HORIZONTAL_POSITIONS[HorizontalPositions.PLACEHOLDER1.ordinal()]);
        }
        else {
            horizontalMotor.setTargetPosition(VERTICAL_POSITIONS[HorizontalPositions.PLACEHOLDER2.ordinal()]);
        }
    }

    public void setClawPosition(ClawPositions clawPosition){
        claw.setPosition(CLAW_POSITIONS[clawPosition.ordinal()]);
    }

    public void setHandPosition(double position){claw.setPosition(position);}
}
