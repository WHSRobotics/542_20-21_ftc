package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.control.ControlConstants;
import org.whitneyrobotics.ftc.teamcode.lib.control.PIDController;
import org.whitneyrobotics.ftc.teamcode.lib.control.PIDFController;
import org.whitneyrobotics.ftc.teamcode.lib.util.Functions;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Wobble {

    ControlConstants linearSlideConstants = new ControlConstants(1, 2, 3); //test for these later
    private PIDFController linearSlideController = new PIDFController(linearSlideConstants);

    private double minLinearSlidePower = 0.2; //test for this

    public String wobbleDesc;

    public Servo armRotator;
    public Servo trapDoor;
    public DcMotorEx wobbleMotor;

    private Toggler wobbleTog = new Toggler(6);
    private Toggler linearSlidePIDStateTog = new Toggler(2);

    // public SimpleTimer delayTimer = new SimpleTimer();

    public enum ArmRotatorPositions {
        FOLDED, IN, OUT
    }

    public enum ClawPositions {
        OPEN, CLOSE
    }

    public enum LinearSlidePositions {
        DOWN, MEDIUM, UP
    }

    public double[] ARM_ROTATOR_POSITIONS = {0, 1, 2}; //folded, in , out; test
    public double[] CLAW_POSITIONS = {0, 0.75}; // open, close;test
    public int[] LINEAR_SLIDE_POSITIONS = {0, 1, 2}; //down, medium, up; test

    public Wobble(HardwareMap wobbleMap) {
        armRotator = wobbleMap.servo.get("clawServo");
        trapDoor = wobbleMap.servo.get("trapDoorServo");
        wobbleMotor = wobbleMap.get(DcMotorEx.class, "wobbleMotor");
    }

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

    public void setArmRotratorPositions(ArmRotatorPositions armRotatorPosition) {
        armRotator.setPosition(ARM_ROTATOR_POSITIONS[armRotatorPosition.ordinal()]);
    }

    public void setClawPosition(ClawPositions clawPosition) {
        trapDoor.setPosition(CLAW_POSITIONS[clawPosition.ordinal()]);
    }

    public void setLinearSlidePosition(LinearSlidePositions linearSlidePosition) {
        int linearSlideState = 0;
        double error = LINEAR_SLIDE_POSITIONS[linearSlidePosition.ordinal()] - wobbleMotor.getCurrentPosition();

        switch (linearSlideState){
            case 0:
                wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                linearSlideController.init(error);
                linearSlideState++;
                break;
            case 1:
                linearSlideController.setConstants(linearSlideConstants);
                linearSlideController.calculate(error, linearSlidePosition.ordinal(), wobbleMotor.getVelocity());

                double linearSlidePower = Functions.map(linearSlideController.getOutput(), 0, LINEAR_SLIDE_POSITIONS[linearSlidePosition.UP.ordinal()], minLinearSlidePower, 1);

                wobbleMotor.setPower(linearSlidePower);
                break;
            default:
                break;
        }
    }

    public void operateWobble(boolean stateFwd, boolean stateBkwd) {
        wobbleTog.changeState(stateFwd, stateBkwd);
        switch (wobbleTog.currentState()) {
            case 0:
                wobbleDesc = "Folded/Take Intake Feed"; // picking up rings
                setArmRotratorPositions(ArmRotatorPositions.FOLDED);
                setClawPosition(ClawPositions.CLOSE);
                setLinearSlidePosition(LinearSlidePositions.DOWN);

            case 1:
                wobbleDesc = "Take Stuff"; // dropping rings on Wobble Goal
                setArmRotratorPositions(ArmRotatorPositions.OUT);
                setClawPosition(ClawPositions.OPEN);
                setLinearSlidePosition(LinearSlidePositions.MEDIUM);

            case 2:
                wobbleDesc = "Carry Stuff"; // grasp Wobble Goal, holding claw near robot while moving
                setArmRotratorPositions(ArmRotatorPositions.IN);
                setClawPosition(ClawPositions.CLOSE);
                setLinearSlidePosition(LinearSlidePositions.MEDIUM);

            case 3:
                wobbleDesc = "Raise to Wall Level"; // raise up to wall height
                setLinearSlidePosition(LinearSlidePositions.UP);

            case 4:
                wobbleDesc = "Extend Out Over Wall"; // extend arm to outside wall
                setArmRotratorPositions(ArmRotatorPositions.OUT);

            case 5:
                wobbleDesc = "Release"; // release Wobble Goal outside wall
                setClawPosition(ClawPositions.OPEN);
            default:
                break;
        }
    }

}
