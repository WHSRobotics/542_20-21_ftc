package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;

public class IntakeTest extends OpMode {
    public Intake testIntake;
    public Toggler powerTog;
    @Override
    public void init() {
        testIntake = new Intake(hardwareMap);
        powerTog = new Toggler(100);
    }

    @Override
    public void loop() {
        powerTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        testIntake.INTAKE_POWER = powerTog.currentState()/100;
        testIntake.operate(gamepad1.right_trigger > 0.01, gamepad1.left_trigger > 0.01);
    }
}
