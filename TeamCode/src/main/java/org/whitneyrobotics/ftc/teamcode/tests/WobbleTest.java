package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Wobble;

@TeleOp(name = "Wobble Test", group = "Tests")
public class WobbleTest extends OpMode {


    public Toggler handTog;
    public Toggler armTog;

    public Wobble testWobble;


    @Override
    public void init() {
        testWobble = new Wobble(hardwareMap);
        handTog = new Toggler(100);
        armTog = new Toggler(100);
    }

    @Override
    public void loop() {
        handTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        armTog.changeState(gamepad1.dpad_right, gamepad1.dpad_left);

        testWobble.setHandPosition(handTog.currentState()/100);
        testWobble.setArmTarget(armTog.currentState()*5);

        telemetry.addData("Hand Pos: ", handTog.currentState()/100);
        telemetry.addData("Arm Targ: ", armTog.currentState()*5);
    }
}
