package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Canister {
    private Servo loader;
    private Servo platform;
    private Toggler canisterToggler = new Toggler(2);
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

    public double[] LOADER_POSITIONS = {0, 90}; // rest, push
    public double[] PLATFORM_POSITIONS = {0, 90}; // flywheel, wobble



    public final double LOADER_REST = LOADER_POSITIONS[Loader_Positions.REST.ordinal()];
    public final double LOADER_PUSH = LOADER_POSITIONS[Loader_Positions.PUSH.ordinal()];

    public SimpleTimer loadTimer = new SimpleTimer();

    public final double PLATFORM_FLYWHEEL = PLATFORM_POSITIONS[Platform_Positions.FLYWHEEL.ordinal()];
    public final double PLATFORM_WOBBLE = PLATFORM_POSITIONS[Platform_Positions.WOBBLE.ordinal()];


    public String canisterState;
    public String platformState;

    public void operateCanister(boolean gamepadInputLoader, boolean gamepadInputPlatform ) {
        canisterToggler.changeState(gamepadInputLoader);
        platformToggler.changeState(gamepadInputPlatform);
        if (canisterToggler.currentState() == 0) {
            canisterState = "Loader Off";
            loader.setPosition(LOADER_REST);
            if (platformToggler.currentState()==0){
                platformState = "Flywheel";
                platform.setPosition(PLATFORM_FLYWHEEL);
            }
            else {
                platformState = "Wobble";
                platform.setPosition(PLATFORM_WOBBLE);
            }
        } else {
            canisterState = "Loader On";
            loader.setPosition(LOADER_PUSH);
            if (platformToggler.currentState()==0){
                platformState = "Flywheel";
                platform.setPosition(PLATFORM_FLYWHEEL);
            }
            else {
                platformState = "Wobble";
                platform.setPosition(PLATFORM_WOBBLE);
            }
        }
    }

    public void loadRing(){
        loadTimer.set(500);
        while (!loadTimer.isExpired()){
            loader.setPosition(LOADER_PUSH);
        }
        loader.setPosition(LOADER_REST);
    }


}