package net.nexusrobotics.swiftbot.movement;

public class PID {
    private double kp, ki, kd;
    private double integralX, integralY;
    private double prevErrorX, prevErrorY;

    public PID(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.integralX = 0;
        this.integralY = 0;
        this.prevErrorX = 0;
        this.prevErrorY = 0;
    }

    public void reset() {
        integralX = 0;
        integralY = 0;
        prevErrorX = 0;
        prevErrorY = 0;
    }

    public double[] compute(double targetX, double targetY, double currentX, double currentY) {
        double errorX = targetX - currentX;
        double errorY = targetY - currentY;

        integralX += errorX;
        integralY += errorY;

        double derivativeX = errorX - prevErrorX;
        double derivativeY = errorY - prevErrorY;

        double controlX = kp * errorX + ki * integralX + kd * derivativeX;
        double controlY = kp * errorY + ki * integralY + kd * derivativeY;

        prevErrorX = errorX;
        prevErrorY = errorY;

        // Ensure control variables are within bounds [0, 1]
        controlX = Math.max(-1, Math.min(1, controlX));
        controlY = Math.max(-1, Math.min(1, controlY));

        return new double[]{controlX, controlY};
    }

}
