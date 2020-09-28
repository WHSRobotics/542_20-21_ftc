package org.whitneyrobotics.ftc.teamcode.lib.purepursuit.strafetotarget;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.motion.RateLimiter;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.PurePursuitRobotConstants;
import org.whitneyrobotics.ftc.teamcode.lib.util.Functions;
import org.whitneyrobotics.ftc.teamcode.subsys.Drivetrain;

public class StrafeFollower {

    StrafePath path;

    public int lastClosestPointIndex = 0;
    public int lastClosestHeadingIndex = 0;
    private int lastIndex = 0;
    private double currentTValue = 0;

    public boolean conditionMet = false;

    private double[] currentTargetWheelVelocities = {0.0, 0.0, 0.0, 0.0};
    private double[] lastTargetWheelVelocities = {0.0, 0.0, 0.0, 0.0};

    private double lastTime;
    private RateLimiter targetVelocityRateLimiter;

    private double kP = PurePursuitRobotConstants.STRAFE_KP;
    private double kV = PurePursuitRobotConstants.STRAFE_KV;
    private double kA = PurePursuitRobotConstants.STRAFE_KV;

    private double trackWidth = Drivetrain.getTrackWidth();
    private double wheelBase = Drivetrain.getWheelBase();

    private boolean inProgress;

    public StrafeFollower (StrafePath path){
        this.path = path;
        targetVelocityRateLimiter = new RateLimiter(PurePursuitRobotConstants.MAX_ACCELERATION, 0);
        lastTime = System.nanoTime()/1E9;
    }

    public double[] calculateMotorPowers(Coordinate currentCoord, double[] currentBackVelocities, double frontRightVelocity) {
        double[] currentWheelVelocities = {currentBackVelocities[1] - (frontRightVelocity - currentBackVelocities[0]), frontRightVelocity, currentBackVelocities[0], currentBackVelocities[1]};

        boolean tFound = false;
        for (int i = lastIndex; i < smoothedPath.length - 1; i++) {
            Double nextTValue = new Double(calculateT(smoothedPath[i], smoothedPath[i + 1], lookaheadDistance));

            if (!tFound && !nextTValue.isNaN() && (nextTValue + i) > (currentTValue + lastIndex)) {
                tFound = true;
                currentTValue = nextTValue;
                lastIndex = i;
            }
        }

        Position calculatedTStartPoint = smoothedPath[lastIndex];
        Position calculatedTEndPoint = smoothedPath[lastIndex + 1];
        lookaheadPoint = Functions.Positions.add(calculatedTStartPoint, Functions.Positions.scale(currentTValue, Functions.Positions.subtract(calculatedTEndPoint, calculatedTStartPoint)));

        int indexOfClosestPoint = calculateIndexOfClosestPoint(smoothedPath);
        int indexOfClosestHeading = calculateIndexOfClosestHeading();

        Position vectorToLookaheadPoint = Functions.Positions.subtract(lookaheadPoint, currentCoord);
        vectorToLookaheadPoint = Functions.field2body(vectorToLookaheadPoint, currentCoord);
        double angleToLookaheadPoint = Math.toDegrees(Math.atan2(vectorToLookaheadPoint.getY(), vectorToLookaheadPoint.getX()));
        angleToLookaheadPointDebug = angleToLookaheadPoint;

        headingController.calculate(targetAngularVelocities[indexOfClosestHeading] - angularVelocity);
        double headingFeedback = headingController.getOutput();

        currentTargetWheelVelocities = calculateTargetWheelVelocities(targetVelocities[indexOfClosestPoint], angleToLookaheadPoint, targetAngularVelocities[indexOfClosestHeading]);

        double deltaTime = System.nanoTime() / 1E9 - lastTime;
        double[] targetWheelAccelerations = new double[4];
        for (int i = 0; i < targetWheelAccelerations.length; i++) {
            targetWheelAccelerations[i] = (currentTargetWheelVelocities[i] - lastTargetWheelVelocities[i]) / deltaTime;
        }
        if (indexOfClosestPoint != smoothedPath.length - 1) {
            double[] feedBack = {currentTargetWheelVelocities[0] - currentWheelVelocities[0], currentTargetWheelVelocities[1] - currentWheelVelocities[1], currentTargetWheelVelocities[2] - currentWheelVelocities[2], currentTargetWheelVelocities[3] - currentWheelVelocities[3]};
            for (int i = 0; i < feedBack.length; i++) {
                feedBack[i] *= kP;
            }

            double[] feedForwardVel = {kV * currentTargetWheelVelocities[0], kV * currentTargetWheelVelocities[1], kV * currentTargetWheelVelocities[2], kV * currentTargetWheelVelocities[3]};
            double[] feedForwardAccel = {kA * targetWheelAccelerations[0], kA * targetWheelAccelerations[1], kA * targetWheelAccelerations[2], kA * targetWheelAccelerations[3]};
            double[] feedForward = {feedForwardVel[0] + feedForwardAccel[0], feedForwardVel[1] + feedForwardAccel[1], feedForwardVel[2] + feedForwardAccel[2], feedForwardVel[3] + feedForwardAccel[3]};
            double[] motorPowers = {Functions.constrain(feedBack[0] + feedForward[0] - headingFeedback, -1, 1), Functions.constrain(feedBack[1] + feedForward[1] + headingFeedback, -1, 1), Functions.constrain(feedBack[2] + feedForward[2] - headingFeedback, -1, 1), Functions.constrain(feedBack[3] + feedForward[3] + headingFeedback, -1, 1)};
            lastTargetWheelVelocities = currentTargetWheelVelocities;
            inProgress = true;
            return motorPowers;
        } else {
            inProgress = false;
        }

        return new double[] {0.0, 0.0, 0.0, 0.0};
    }

}
