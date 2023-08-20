package net.nexusrobotics.swiftbot.movement;

import net.nexusrobotics.swiftbot.odometry.Odometry;
import net.nexusrobotics.swiftbot.odometry.OdometryWithoutOdometry;

import java.util.HashMap;

public class Process {
    static PID pid = new PID(0.5, 0.1, 0.2);
    public static double[] process(float targetHeading, OdometryWithoutOdometry odometry, float targetX, float targetY, double tolerance){
        float toRotate = targetHeading - odometry.localization.currentHeading;
        /*ffloat toX = targetX - odometry.localization.currentX;
        float toY = targetY - odometry.localization.currentY;

        loat forwardX = getOneFromForward(odometry.localization.currentHeading).get("x");
        float forwardY = getOneFromForward(odometry.localization.currentHeading).get("y");
        float rightX = getOneFromForward(odometry.localization.currentHeading + 90).get("x");
        float rightY = getOneFromForward(odometry.localization.currentHeading + 90).get("y");*/
        //ignore rotations for now
        /*if(toRotate > 0){

        }else if(toX > 0 || toY > 0){

        }*/
        double[] transformedTargets = calculateNewCoordinates(Math.toRadians(odometry.localization.currentHeading), targetX - odometry.localization.currentX, targetX - odometry.localization.currentX);
        double[] controls = pid.compute(transformedTargets[0], transformedTargets[1], 0, 0);
        return controls;
    }

    static HashMap<String, Float> getOneFromForward(float heading){
        heading = (float) Math.toRadians(heading);
        HashMap<String, Float> map = new HashMap<>();
        map.put("x", (float)Math.sin(heading));
        map.put("y", (float)Math.cos(heading));
        return map;
    }

    public static double[] calculateNewCoordinates(double headingChange, double pointX, double pointY) {
        double newPointX = pointX * Math.cos(headingChange) - pointY * Math.sin(headingChange);
        double newPointY = pointX * Math.sin(headingChange) + pointY * Math.cos(headingChange);
        return new double[] { newPointX, newPointY };
    }
}
