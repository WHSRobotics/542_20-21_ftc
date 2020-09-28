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
    public void operateIntakeWheel(boolean gamepadInput) {
        wheelToggler.changeState(gamepadInput);
        intakeState = wheelToggler.currentState();
        switch (intakeState){
            case 0:
                wheelIntake.setPower(0);
                break;
            case 1:
                wheelIntake.setPower(INTAKE_POWER);
                break;
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
