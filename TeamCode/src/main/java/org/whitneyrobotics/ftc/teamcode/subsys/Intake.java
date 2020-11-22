package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Intake {

    private DcMotorEx wheelIntake;

    private Servo dropdown;

    private Toggler wheelToggler = new Toggler(2);

    public int intakeState;

    public  double INTAKE_POWER = 0.45; //change to final after testing

    public Intake(HardwareMap intakeWheel) {
        wheelIntake = intakeWheel.get(DcMotorEx.class, "Wheel Intake");
        dropdown = intakeWheel.servo.get("Intake Dropdown");
    }
    public enum dropdown_position {
        UP, DOWN
    }

    public static final double DROPDOWN_UP = 0; //placeholder
    public static final double DROPDOWN_DOWN = 0.5; //placeholder

    public double[] dropdownPositions ={DROPDOWN_UP, DROPDOWN_DOWN};

    public double dropdownUp = dropdownPositions[dropdown_position.UP.ordinal()];
    public  double dropdownDown = dropdownPositions[dropdown_position.DOWN.ordinal()];

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
    public void setDropdown(double position){ ;
        dropdown.setPosition(position);
    }
    public void setIntakePower (double power){
        wheelIntake.setPower(power);
    }
}
