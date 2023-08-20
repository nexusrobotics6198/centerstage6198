package net.nexusrobotics.swiftbot.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import net.nexusrobotics.swiftbot.constants.MecanumConstants;
import net.nexusrobotics.swiftbot.localization.Localization;
import net.nexusrobotics.swiftbot.robot.MecanumRobot;

public class OdometryWithoutOdometry{
    MecanumRobot robot;
    MecanumConstants constants;
    public Localization localization;
    float odoDiameter;

    public OdometryWithoutOdometry(MecanumRobot robot, Localization localization){
        this.constants = robot.constants;
        this.robot = robot;
        this.localization = localization;
        this.odoDiameter = constants.deadwheeldiameter;
        DcMotor leftMotor = robot.getFrontLeft();
        DcMotor rightMotor = robot.getFrontRight();
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Switch to RUN_WITHOUT_ENCODER mode for continuous tracking
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    int threshold = 10;
    public void init(){
        new Thread(()-> {
            while (true) {
                localization.setX((float)robot.getIMU().getPosition().x);
                localization.setY((float)robot.getIMU().getPosition().y);
                localization.setHeading(0);
                System.out.println(localization.currentX + ":" + localization.currentY + " Heading: " + localization.currentHeading);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
