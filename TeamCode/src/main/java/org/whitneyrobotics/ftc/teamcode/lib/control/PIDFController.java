package org.whitneyrobotics.ftc.teamcode.lib.control;

import org.whitneyrobotics.ftc.teamcode.lib.motion.MotionProfile;
import org.whitneyrobotics.ftc.teamcode.lib.util.Functions;

public class PIDFController {
    ControlConstants constants;

    double lastKnownTime;
    double lastKnownError;
    double integral;
    double derivative;
    double velocity;

    double error;

    MotionProfile motionProfile;

    public PIDFController (MotionProfile motionProfile){
        this.motionProfile = motionProfile;
    }

    public PIDFController (MotionProfile motionProfile, ControlConstants constants ){
        this.motionProfile = motionProfile;
        setConstants(constants);
    }

    public void setConstants(ControlConstants constants){
        this.constants = constants;
    }

    public void init(double initialError) {
        lastKnownTime = System.nanoTime() / 1E9;
        lastKnownError = initialError;
        integral = 0;
    }

    public void calculate(double error, double currentPosition){
       this.error = error;
       double deltaError = error - lastKnownError;

       double currentTime = System.nanoTime() / 1E9;
       double deltaTime = currentTime - lastKnownTime;
       lastKnownTime = currentTime;

       integral += error * deltaTime;
       derivative = deltaError/deltaTime;

       velocity = motionProfile.getTargetVelocities()[velocityAtClosestPoint(currentPosition)];
    }

    public double getOutput(){
        double output = constants.getkP() * error + constants.getkD() * derivative + constants.getkI() * integral + constants.getkV() * velocity;
        return output;
    }

    public int velocityAtClosestPoint(double position){
        double[] differenceArray = new double[motionProfile.getPoints().length];
        for (int i = 0; i < motionProfile.getPoints().length; i++) {
            differenceArray[i] = motionProfile.getPoints()[i] - position;
        }
        return Functions.calculateIndexOfSmallestValue(differenceArray);
    }

}
