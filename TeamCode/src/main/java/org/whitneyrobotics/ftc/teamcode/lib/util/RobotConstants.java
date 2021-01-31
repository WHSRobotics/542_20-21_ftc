package org.whitneyrobotics.ftc.teamcode.lib.util;

//import com.acmerobotics.dashboard.config.Config;

import org.whitneyrobotics.ftc.teamcode.lib.control.ControlConstants;

public class RobotConstants {
    //Drivetrain
    public static double DEADBAND_DRIVE_TO_TARGET = 24.5;
    public static double DEADBAND_ROTATE_TO_TARGET = 1.0;
    public static double drive_min = .2;//.1245;
    public static double drive_max = 0.4;//.6;
    public static double rotate_min = 0.2;
    public static double rotate_max = 1.0;

    public static ControlConstants DRIVE_CONSTANTS = new ControlConstants(1.7, 0.7, 0.8);
    public static ControlConstants ROTATE_CONSTANTS = new ControlConstants(1.1, 0.07542, 0.15);

    //Outtake
    public static double OUTTAKE_MAX_VELOCITY = 1800;
    public static ControlConstants.FeedforwardFunction flywheelKF = (double currentPosition, double currentVelocity) ->  1/OUTTAKE_MAX_VELOCITY;
    public static ControlConstants FLYWHEEL_CONSTANTS = new ControlConstants(1,1,2, flywheelKF);

    public static double rotateTestAngle = 180;
    public static boolean rotateOrientation = true;


}
