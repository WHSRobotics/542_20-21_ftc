package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Canister {
    private Servo loader;
    private Servo platform;
    private Toggler loaderToggler = new Toggler(2);
    public Toggler platformToggler = new Toggler(2);

    public enum PlatformPositions {
        FLYWHEEL, WOBBLE
    }

    public enum LoaderPositions {
        REST, PUSH
    }

    public double[] LOADER_POSITIONS = {0, 0.90}; // rest, push
    public double[] PLATFORM_POSITIONS = {0, 0.90}; // flywheel, wobble

    public SimpleTimer loadTimer = new SimpleTimer();

    public String canisterState;
    public String platformState;

    public Canister(HardwareMap canisterMap) {
        loader = canisterMap.servo.get("loaderServo");
        platform = canisterMap.servo.get("platform");
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

    public void operatePlatform(boolean gamepadInputPlatform ){
        platformToggler.changeState(gamepadInputPlatform);
        if (platformToggler.currentState()==0){
            platformState = "Flywheel";
            platform.setPosition(PLATFORM_POSITIONS[PlatformPositions.FLYWHEEL.ordinal()]);
        }
        else {
            platformState = "Wobble";
            platform.setPosition(PLATFORM_POSITIONS[PlatformPositions.WOBBLE.ordinal()]);
        }
    }

    public void loadRing(){
        loadTimer.set(500);
        if (!loadTimer.isExpired()){
            loader.setPosition(LOADER_POSITIONS[LoaderPositions.PUSH.ordinal()]);
        } else {
            loader.setPosition(LOADER_POSITIONS[LoaderPositions.REST.ordinal()]);
        }
    }

    public void setLoaderPosition(double position){loader.setPosition(position);}
    public void setPlatformPosition(double position){platform.setPosition(position);}
}