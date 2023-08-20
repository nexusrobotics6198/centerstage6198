package net.nexusrobotics.swiftbot.odometry;

import net.nexusrobotics.swiftbot.constants.MecanumConstants;
import net.nexusrobotics.swiftbot.localization.Localization;
import net.nexusrobotics.swiftbot.robot.MecanumRobot;

public class Odometry {
    MecanumRobot robot;
    MecanumConstants constants;
    public Localization localization;
    int timederivative = 10;
    float odoDiameter;

    public Odometry(MecanumRobot robot, Localization localization){
        this.constants = robot.constants;
        this.robot = robot;
        this.localization = localization;
        this.odoDiameter = constants.deadwheeldiameter;
    }

    float calcAbsWeightX(float heading){
        float heading90 = heading%180;
        if(heading90 > 90){
            heading90 = 180 - heading90;
        }
        return (1 - (heading90/90));
    }
    int threshold = 10;
    public void init(){
        new Thread(()->{
            int lastPosP1 = robot.getOdoParallel1().getCurrentPosition();
            int lastPosP2 = robot.getOdoParallel2().getCurrentPosition();
            int lastPosPerp = robot.getOdoPerp().getCurrentPosition();
            while(true){
                float dP1 = robot.getOdoParallel1().getCurrentPosition() - lastPosP1;
                float dP2 = robot.getOdoParallel2().getCurrentPosition() - lastPosP2;
                float dPerp = robot.getOdoPerp().getCurrentPosition() - lastPosPerp;
                double odoCircumference = odoDiameter*Math.PI;

                double dOdoPerp = odoCircumference*(dPerp/constants.ticks_per_rotation); // in same unit as diameter (inches)
                double dOdoP1 = odoCircumference*(dP1/constants.ticks_per_rotation); // in same unit as diameter (inches)
                double dOdoP2 = odoCircumference*(dP2/constants.ticks_per_rotation); // in same unit as diameter (inches)


                if(/*(dP1 > 0)&&(dP2 < 0) || (dP2 > 0)&&(dP1 < 0)*/false){ // check if parallel odo wheels are moving in opposite directions
                    // 1 is right, 2 is left
                    int direction = dPerp > 0 ? 2 : 1; // may be debug idk
                    localization.setHeading((float) (localization.currentHeading + (360*(dOdoPerp/(2*constants.odoperptocenter*Math.PI)))));
                }else/* if((dP1 - dP2 > threshold && ((dP1 > 0 && dP2 > 0) || (dP1 < 0 && dP2 < 0)))||(dP1 + dP2 > threshold && ((dP1 < 0 && dP2 > 0) || (dP1 > 0 && dP2 < 0))))*/ {
                    // update heading and lateral positioning

                    float yMultiplier;
                    float xMultiplier;
                    float perpHeadingCalc = (float) (localization.currentHeading + (360*(dOdoPerp/(2*constants.odoperptocenter*Math.PI))));
                    float latHeadingCalc;
                    // positive - positive heading change | negative - negative heading change
                    float difference = (float) (dOdoP1 - dOdoP2);
                    latHeadingCalc = (float) (360*((difference/(Math.PI * (2*constants.deadWheelDistance)))) + localization.currentHeading);
                    double strafe = ((perpHeadingCalc - latHeadingCalc)/360)*(2*constants.odoperptocenter*Math.PI);
                    localization.setHeading(latHeadingCalc);
                    if((localization.currentHeading >= 0 && localization.currentHeading < 90) || (localization.currentHeading > 270 && localization.currentHeading <= 360)){
                        // positive y increase
                        yMultiplier = 1;
                    }else{
                        yMultiplier = -1;
                    }
                    if((localization.currentHeading >= 0 && localization.currentHeading < 180)){
                        // positive x increase
                        xMultiplier = 1;
                    }else{
                        xMultiplier = -1;
                    }

                    float y = calcAbsWeightX(localization.currentHeading);
                    yMultiplier = y*yMultiplier;
                    xMultiplier = (1-y)*xMultiplier;

                    //double dD = (((dOdoP1 + dOdoP2)/2) != 0) ? ((((dOdoP1 + dOdoP2)/2)/Math.abs((dOdoP1 + dOdoP2)/2))*0.02) + (dOdoP1 + dOdoP2)/2 : 0;
                    double dD = Math.abs(Math.max(dOdoP1, dOdoP2) - Math.abs(dOdoP1 - dOdoP2));
                    double dX;
                    double dY;
                    if(xMultiplier == 0){
                        dX = 0;
                        dY = dD;
                    }else if(yMultiplier == 0){
                        dX = dD;
                        dY = 0;
                    }else{
                        dX = dD / Math.sqrt(1 + Math.pow(yMultiplier,2)/Math.pow(xMultiplier,2));
                        dY = (yMultiplier / xMultiplier) * dX;
                    }
                    dX *= (xMultiplier != 0) ? xMultiplier/Math.abs(xMultiplier) : 1;
                    dY *= (yMultiplier != 0) ? yMultiplier/Math.abs(yMultiplier) : 1;
                    int strafeSignX = 1;
                    int strafeSignY = 1;
                    if(localization.currentHeading >= 0 && localization.currentHeading <= 180){
                        strafeSignY = -1;
                    }
                    if(localization.currentHeading >= 90 && localization.currentHeading <= 270){
                        strafeSignX = -1;
                    }
                    float temp = xMultiplier;
                    xMultiplier = yMultiplier*strafeSignX;
                    yMultiplier = temp*strafeSignY;
                    dD = strafe;
                    if(xMultiplier == 0){
                        dX += 0;
                        dY += dD*strafeSignY;
                    }else if(yMultiplier == 0){
                        dX += dD*strafeSignX;
                        dY += 0;
                    }else{
                        double dXT = (dD / Math.sqrt(1 + Math.pow(yMultiplier,2)/Math.pow(xMultiplier,2)));
                        double dYT = ((yMultiplier / xMultiplier) * /*(dD / Math.sqrt(1 + Math.pow(yMultiplier,2)/Math.pow(xMultiplier,2)))*/dXT);
                        dX += dXT;
                        dY += dYT;
                    }
                    localization.setX((float) (localization.currentX + /*((((dOdoP1+dOdoP2)/2))*xMultiplier))*/dX));
                    localization.setY((float) (localization.currentY + /*((((dOdoP1+dOdoP2)/2))*yMultiplier))*/dY));
                    System.out.println(localization.currentX + ":" + localization.currentY + " Heading: " + localization.currentHeading);
                }

                lastPosP1 = robot.getOdoParallel1().getCurrentPosition();
                lastPosP2 = robot.getOdoParallel2().getCurrentPosition();
                lastPosPerp = robot.getOdoPerp().getCurrentPosition();
                try {
                    Thread.sleep(timederivative);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
