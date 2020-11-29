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
    public Canister(HardwareMap canisterMap) {
        loader = canisterMap.servo.get("loaderServo");
        platform = canisterMap.servo.get("platform");
    }
    public enum Platform_Positions{
        FLYWHEEL, WOBBLE
    }
    public enum Loader_Positions {
        REST, PUSH
    }

    public double[] LOADER_POSITIONS = {0, 0.90}; // rest, push
    public double[] PLATFORM_POSITIONS = {0, 0.90}; // flywheel, wobble





    public SimpleTimer loadTimer = new SimpleTimer();



    public String canisterState;
    public String platformState;

    public void operateCanister(boolean gamepadInputLoader, boolean gamepadInputPlatform ) {
        loaderToggler.changeState(gamepadInputLoader);
        platformToggler.changeState(gamepadInputPlatform);
        if (loaderToggler.currentState() == 0) {
            canisterState = "Loader Off";
            loader.setPosition(LOADER_POSITIONS[Loader_Positions.REST.ordinal()]);
            if (platformToggler.currentState()==0){
                platformState = "Flywheel";
                platform.setPosition(PLATFORM_POSITIONS[Platform_Positions.FLYWHEEL.ordinal()]);
            }
            else {
                platformState = "Wobble";
                platform.setPosition(PLATFORM_POSITIONS[Platform_Positions.WOBBLE.ordinal()]);
            }
        } else {
            canisterState = "Loader On";
            loader.setPosition(LOADER_POSITIONS[Loader_Positions.PUSH.ordinal()]);
            if (platformToggler.currentState()==0){
                platformState = "Flywheel";
                platform.setPosition(PLATFORM_POSITIONS[Platform_Positions.FLYWHEEL.ordinal()]);
            }
            else {
                platformState = "Wobble";
                platform.setPosition(PLATFORM_POSITIONS[Platform_Positions.WOBBLE.ordinal()]);
            }
        }
    }

    public void loadRing(){
        loadTimer.set(500);
        while (!loadTimer.isExpired()){
            loader.setPosition(LOADER_POSITIONS[Loader_Positions.PUSH.ordinal()]);
        }
        loader.setPosition(LOADER_POSITIONS[Loader_Positions.REST.ordinal()]);
    }

    public void setLoaderPosition(double position){loader.setPosition(position);}
    public void setPlatformPosition(double position){platform.setPosition(position);}
}