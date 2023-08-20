package net.nexusrobotics.swiftbot.drive;

import net.nexusrobotics.swiftbot.localization.Localization;
import net.nexusrobotics.swiftbot.movement.Process;
import net.nexusrobotics.swiftbot.odometry.Odometry;
import net.nexusrobotics.swiftbot.odometry.OdometryWithoutOdometry;
import net.nexusrobotics.swiftbot.robot.MecanumRobot;

import java.util.HashMap;

public class MecanumDrive {
    public OdometryWithoutOdometry odometry;
    MecanumRobot robot;
    float targetX;
    float targetY;
    float targetHeading;

    public MecanumDrive(OdometryWithoutOdometry odometry, MecanumRobot robot){
        this.odometry = odometry;
        this.robot = robot;
    }
    public void init(){
        odometry.init();
        targetX = odometry.localization.currentX;
        targetY = odometry.localization.currentY;
        loop();
    }

    void loop(){
        new Thread(()->{
            while(true){
                double[] proc = Process.process(targetHeading, odometry, targetX, targetY, 0.01);
                robot.move(proc[0], proc[1], 0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateTarget(float x, float y){
        this.targetX = x;
        this.targetY = y;
    }

    HashMap<String, Float> getOneFromForward(float heading){
        heading = (float) Math.toRadians(heading);
        HashMap<String, Float> map = new HashMap<>();
        map.put("x", (float)Math.sin(heading));
        map.put("y", (float)Math.cos(heading));
        return map;
    }
}
