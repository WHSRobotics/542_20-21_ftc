package org.whitneyrobotics.ftc.teamcode.lib.control;

public class ControlConstants {
    public double kP;
    public double kI;
    public double kD;

    public double kV;

    public ControlConstants(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public ControlConstants(double kP, double kI, double kD, double kV) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kV = kV;
    }



    public double getkP() {
        return kP;
    }

    public void setkP(double kP) {
        this.kP = kP;
    }

    public double getkI() {
        return kI;
    }

    public void setkI(double kI) {
        this.kI = kI;
    }

    public double getkD() {
        return kD;
    }

    public void setkD(double kD) {
        this.kD = kD;
    }

    public double getkV() {
        return kV;
    }


    public void setkV(double kV) {
        this.kV = kV;
    }
}
