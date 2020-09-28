package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Canister {
    private Servo loader;
    private Toggler canisterToggler = new Toggler(2);

    public Canister (HardwareMap canisterMap){
        loader = canisterMap.servo.get("canisterServo");
    }

    public enum LoaderPositions{
        REST, PUSH
    }

    public double[]LOADER_POSITIONS={0, 90} ; // rest, push
    public final double LOADER_REST = LOADER_POSITIONS[LoaderPositions.REST.ordinal()];
    public final double LOADER_PUSH = LOADER_POSITIONS[LoaderPositions.PUSH.ordinal()];

    public void operateCanister(boolean gamepadInput1) {
        canisterToggler.changeState(gamepadInput1);
        if (canisterToggler.currentState()==0) {
            loader.setPosition(LOADER_REST);
        }

        else {
            loader.setPosition(LOADER_PUSH);
        }
    }
}
