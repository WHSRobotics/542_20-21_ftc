package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Flywheel {
    public final double POWER_SHOT_TARGET_HEIGHT = 584.2;
    public final double MID_GOAL_HEIGHT = 685.8;
    public final double HIGH_TARGET_HEIGHT = 901.7;
    DcMotorEx launcher;
    public final double FLYWHEEL_POWER = 0.5;
    public Flywheel(HardwareMap flyMap){
        launcher = flyMap.get(DcMotorEx.class, "FlyWheel");
    }
    Toggler flyWheelTog = new Toggler(2);
    public int launchState;
    //public void calculateLaunchHeading ()
    public void operate(boolean togInc, boolean togDec){
        flyWheelTog.changeState(togInc, togDec);
        launchState = flyWheelTog.currentState();
        switch (launchState){
            case 0:
                launcher.setPower(0);
                break;
            case 1:
                launcher.setPower(FLYWHEEL_POWER);
                break;
        }
    }
    public void On (){
        launcher.setPower(0);
    }
    public  void Off (){
        launcher.setPower(FLYWHEEL_POWER);
    }
}
