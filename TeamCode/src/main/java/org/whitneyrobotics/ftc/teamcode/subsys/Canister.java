package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Canister {
    Servo loader;
    public Canister (HardwareMap canisterMap){
        loader=canisterMap.servo.get("canisterServo");
    }
    public enum Loader_Positions{
        REST, PUSH
    }
    public double[]LOADER_POSITIONS={0, 90} ; // rest, push
    public final double LOADER_REST = LOADER_POSITIONS[Loader_Positions.REST.ordinal()];
    public final double LOADER_PUSH = LOADER_POSITIONS[Loader_Positions.PUSH.ordinal()];
    Toggler loaderPositionsToggler = new Toggler(2);
    public void operate (boolean switcher){
        
    }
}
