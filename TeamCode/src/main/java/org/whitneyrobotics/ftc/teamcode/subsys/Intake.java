package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Intake {

    private DcMotorEx wheelIntake;

    private Toggler wheelToggler = new Toggler(2);

    public int intakeState;

    public final double INTAKE_POWER = 0.45;

    public Intake(HardwareMap intakeWheel) {
        wheelIntake = intakeWheel.get(DcMotorEx.class, "Wheel Intake");
    }

    public String intakeStateDescription;
    public void operate(boolean gamepadInput1, boolean gamepadInput2) {
        wheelToggler.changeState(gamepadInput1);
        if (gamepadInput2) {
            wheelIntake.setPower(-INTAKE_POWER);
            intakeStateDescription = "Reverse Intake";
        }
        else if (wheelToggler.currentState() == 1) {
            wheelIntake.setPower(INTAKE_POWER);
            intakeStateDescription = "Forward Intake";
        }
        else {
            wheelIntake.setPower(0.0);
            intakeStateDescription = "Intake Off";
        }

    }
    // for use in Auto
    public void On (){
        wheelIntake.setPower(INTAKE_POWER);
    }
    public void Off(){
        wheelIntake.setPower(0);
    }
}
