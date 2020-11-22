package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class WobbleTest extends OpMode {

    public Servo hand;
    public DcMotor arm;

    public Toggler handTog;
    public Toggler armTog;

    public double handPos;
    public double armPower;

    @Override
    public void init() {
        hand = hardwareMap.servo.get("clawServo");
        arm = (DcMotor) hardwareMap.dcMotor.get("armMotor");
        handTog = new Toggler(100);
        armTog = new Toggler(100);
    }

    @Override
    public void loop() {
        handTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        armTog.changeState(gamepad1.dpad_right, gamepad1.dpad_left);

        handPos = handTog.currentState()/100;
        armPower = armTog.currentState()/100;

        hand.setPosition(handPos);
        arm.setPower(armPower);

        telemetry.addData("Hand Pos: ", handPos);
        telemetry.addData("Arm Pow: ", armPower);
    }
}
