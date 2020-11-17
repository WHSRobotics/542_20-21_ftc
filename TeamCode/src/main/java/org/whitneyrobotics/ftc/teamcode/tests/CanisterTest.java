package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Canister;

public class CanisterTest extends OpMode {
    private Servo loader;
    private Servo platform;

    public Toggler platformTestTog;
    public Toggler loaderTestTog;

    public double loaderSetting;
    public double platformSetting;

    @Override
    public void init() {
        loader = hardwareMap.servo.get("loaderServo");
        platform = hardwareMap.servo.get("platformServo");
        platformTestTog = new Toggler(100);
        loaderTestTog = new Toggler(100);

    }

    @Override
    public void loop() {
        loaderTestTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        platformTestTog.changeState(gamepad1.dpad_right, gamepad1.dpad_left);
        loaderSetting = loaderTestTog.currentState()/100;
        platformSetting = platformTestTog.currentState()/100;
        loader.setPosition(loaderSetting);
        platform.setPosition(platformSetting);
    }
}
