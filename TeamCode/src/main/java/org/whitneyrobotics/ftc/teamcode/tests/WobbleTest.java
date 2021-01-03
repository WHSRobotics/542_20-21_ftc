package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Wobble;

@TeleOp(name = "Wobble Test", group = "Tests")
public class WobbleTest extends OpMode {


    public Toggler armRotatorTog;
    public Toggler clawTog;
    public Toggler linearSlideTog;
    public Wobble testWobble;



    @Override
    public void init() {
        testWobble = new Wobble(hardwareMap);
        armRotatorTog = new Toggler(100);
        clawTog = new Toggler(100);
        linearSlideTog = new Toggler(100);
    }

    @Override
    public void loop() {
        armRotatorTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        clawTog.changeState(gamepad1.dpad_right, gamepad1.dpad_left);
        linearSlideTog.changeState(gamepad1.right_bumper, gamepad1.left_bumper);

        testWobble.armRotator.setPosition(armRotatorTog.currentState()/100);
        testWobble.trapDoor.setPosition(clawTog.currentState()/100);
        testWobble.linearSlide.setTargetPosition(linearSlideTog.currentState()*5);

        telemetry.addData("Arm Rotator Pos: ", armRotatorTog.currentState()/100);
        telemetry.addData("Claw Pos: ", clawTog.currentState()/100);
        telemetry.addData("Linear Slide Targ: ", linearSlideTog.currentState()*5);
    }
}
