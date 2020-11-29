package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;

public class IntakeTest extends OpMode {
    public Intake testIntake;
    public Toggler powerTog;
    public Toggler dropdownTog;

    @Override
    public void init() {
        testIntake = new Intake(hardwareMap);
        powerTog = new Toggler(100);
        dropdownTog = new Toggler(100);

    }

    @Override
    public void loop() {
        powerTog.changeState(gamepad1.right_trigger > 0.01, gamepad1.left_trigger > 0.01);
        dropdownTog.changeState(gamepad1.b, gamepad1.a);
        testIntake.setIntakePower(powerTog.currentState()/100);
        testIntake.setDropdownPosition(dropdownTog.currentState()/100);

        telemetry.addData("Wheel Power: ", powerTog.currentState()/100);
        telemetry.addData("Dropdown Position", dropdownTog.currentState()/100);

    }
}
