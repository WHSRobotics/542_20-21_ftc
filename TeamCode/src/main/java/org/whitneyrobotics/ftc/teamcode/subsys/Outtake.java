package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class Outtake {
    Flywheel flywheel;
    Servo flap;
    public Outtake(HardwareMap outtakeMap){
        flywheel = new Flywheel(hardwareMap);
        flap =  hardwareMap.servo.get("flap");
    }
}
