package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;

@TeleOp(name = "Outtake Test", group = "Tests")
public class OuttakeTest extends OpMode {

    public Outtake testOuttake;

    double power;

    int i = 0;

    @Override
    public void init() {
        testOuttake = new Outtake(hardwareMap);

        power = 0;
    }

    @Override
    public void loop() {
        i++;

        if(i%10 == 0){
            if(gamepad1.a) {
                power += 0.01;
            }else if (gamepad1.b){
                power -= 0.01;
            }
        }

        testOuttake.setLauncherPower(power);

        telemetry.addData("Flywheel Power: ", power);
    }
}
