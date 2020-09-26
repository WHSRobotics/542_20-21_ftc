package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Intake {

    private DcMotorEx wheelIntake;

    private Toggler wheelToggler = new Toggler(2);

    public final double INTAKE_POWER = 0.45;

    public Intake(HardwareMap intakeWheel) {
        wheelIntake = intakeWheel.get(DcMotorEx.class, "Wheel Intake");
    }

    public void operateIntakeWheel(boolean gamepadInput1) {
        wheelToggler.changeState(gamepadInput1);
        if (wheelToggler.currentState()==0) {
            wheelIntake.setPower(0);
        }

        else {
            wheelIntake.setPower(INTAKE_POWER);
        }
    }
}
