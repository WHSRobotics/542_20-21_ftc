package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class OuttakeTest extends OpMode {
    public DcMotorEx flywheel;
    public Servo flap;

    public Toggler flywheelTestTog;
    public Toggler flapTestTog;

    public double flywheelPower;
    public double flapSetting;

    @Override
    public void init() {
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flap = hardwareMap.servo.get("flapServo");

        flywheelTestTog = new Toggler(100);
        flapTestTog = new Toggler(100);
    }

    @Override
    public void loop() {
        flywheelTestTog.changeState(gamepad2.right_trigger>0.01, gamepad2.left_trigger>0.01);
        flapTestTog.changeState(gamepad2.right_bumper, gamepad2.left_bumper);

        flywheelPower = flywheelTestTog.currentState()/100;
        flapSetting = flapTestTog.currentState()/100;

        flywheel.setPower(flywheelPower);
        flap.setPosition(flapSetting);

        telemetry.addData("Flywheel Power: ", flywheelPower);
        telemetry.addData("Flap Setting: ", flapSetting);
    }
}
