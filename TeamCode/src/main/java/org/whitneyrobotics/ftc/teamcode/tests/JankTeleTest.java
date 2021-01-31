package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.whitneyrobotics.ftc.teamcode.subsys.JankWobble;

@TeleOp(name = "Jank Wobble Test")
public class JankTeleTest extends OpMode {
    JankWobble wobble;
    @Override
    public void init() {
        wobble = new JankWobble(hardwareMap);
    }

    @Override
    public void loop() {
        wobble.operate(gamepad1.y, gamepad1.x);
        telemetry.addData("Enc Position", wobble.wobbleMotor.getCurrentPosition());
    }
}
