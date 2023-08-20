package net.nexusrobotics.swiftbot.robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareDevice;


import net.nexusrobotics.swiftbot.constants.MecanumConstants;

import java.nio.ByteBuffer;
import java.util.HashMap;

public abstract class MecanumRobot {
    public MecanumConstants constants;
    public HashMap<String, HardwareDevice> customParts = new HashMap<>();

    public MecanumRobot(MecanumConstants constants){
        this.constants = constants;
    }

    public abstract DcMotor getBackLeft();
    public abstract DcMotor getBackRight();
    public abstract DcMotor getFrontLeft();
    public abstract DcMotor getFrontRight();
    public abstract BNO055IMU getIMU();
    public abstract DcMotorEx getOdoParallel1();
    public abstract DcMotorEx getOdoParallel2();
    public abstract DcMotorEx getOdoPerp();
    public abstract HardwareDevice getCustomPartCast(Class<? extends HardwareDevice> partClass, String part);
    public abstract HardwareDevice getCustomPart(String part);
    public void move(double x, double y, double r){
        double drive = -y/3.5;
        double strafe = (x * 1.1)/4.5;
        double turn = (r)/4.9;
        double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 0.65);
        double frontLeftPower = (drive + strafe + turn) / denominator;
        double backLeftPower = (drive - strafe + turn) / denominator;
        double frontRightPower = (drive - strafe - turn) / denominator;
        double backRightPower = (drive + strafe - turn) / denominator;

        getFrontLeft().setPower(frontLeftPower);
        getFrontRight().setPower(frontRightPower);
        getBackLeft().setPower(backLeftPower);
        getBackRight().setPower(backRightPower);
    }

    public void registerPart(String name, HardwareDevice part){
        customParts.put(name, part);
    }
}
