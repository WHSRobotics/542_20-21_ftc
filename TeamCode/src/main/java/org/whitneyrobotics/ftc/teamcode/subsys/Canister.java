package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Canister {
    private Servo loader;
    private Toggler loaderToggler = new Toggler(2);


    public enum LoaderPositions {
        REST, PUSH
    }

    public double[] LOADER_POSITIONS = {0, 0.90}; // rest, push

    public SimpleTimer loadTimer = new SimpleTimer();

    public String canisterState;

    public Canister(HardwareMap canisterMap) {
        loader = canisterMap.servo.get("loaderServo");
    }

    public void operateLoader(boolean gamepadInputLoader) {
        loaderToggler.changeState(gamepadInputLoader);
        if (loaderToggler.currentState() == 0) {
            canisterState = "Loader Off";
            loader.setPosition(LOADER_POSITIONS[LoaderPositions.REST.ordinal()]);
        } else {
            canisterState = "Loader On";
            loader.setPosition(LOADER_POSITIONS[LoaderPositions.PUSH.ordinal()]);

        }
    }


    public void loadRing() {
        int loadState = 0;
        switch (loadState) {
            case 0:
                loadTimer.set(500);
                loadState++;
                break;
            case 1:
                if (!loadTimer.isExpired()) {
                    loader.setPosition(LOADER_POSITIONS[LoaderPositions.PUSH.ordinal()]);
                } else {
                    loader.setPosition(LOADER_POSITIONS[LoaderPositions.REST.ordinal()]);
                }
                break;
            default:
                break;
        }
    }

    public void setLoaderPosition(double position) { loader.setPosition(position); }
}