package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Canister;

@TeleOp(name = "Canister Test", group = "Tests")
public class CanisterTest extends OpMode {
    private Canister testCanister;

    public Toggler loaderTestTog;


    @Override
    public void init() {
        testCanister = new Canister(hardwareMap);
        loaderTestTog = new Toggler(100);

    }

    @Override
    public void loop() {
        loaderTestTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);



        testCanister.setLoaderPosition(loaderTestTog.currentState()/100);


        telemetry.addData("Loader Setting:" , loaderTestTog.currentState()/100);
    }
}
