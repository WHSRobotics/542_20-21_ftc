package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;

public class OuttakeTest extends OpMode {

    public Outtake testOuttake;

    public Toggler flywheelTestTog;
    public Toggler flapTestTog;

    @Override
    public void init() {
        testOuttake = new Outtake(hardwareMap);

        flywheelTestTog = new Toggler(100);
        flapTestTog = new Toggler(100);
    }

    @Override
    public void loop() {
        flywheelTestTog.changeState(gamepad2.right_trigger>0.01, gamepad2.left_trigger>0.01);
        flapTestTog.changeState(gamepad2.right_bumper, gamepad2.left_bumper);

        testOuttake.setLauncherPower(flywheelTestTog.currentState()/100);
        testOuttake.setFlapPosition(flapTestTog.currentState()/100);

        telemetry.addData("Flywheel Power: ", flywheelTestTog.currentState()/100);
        telemetry.addData("Flap Setting: ", flapTestTog.currentState()/100);
    }
}
