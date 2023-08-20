package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import net.nexusrobotics.swiftbot.constants.MecanumConstants;
import net.nexusrobotics.swiftbot.robot.MecanumRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;

public class NexusMecanumDrive extends MecanumRobot {

    public DcMotor leftFront;
    public DcMotor leftBack;
    public DcMotor rightFront;
    public DcMotor rightBack;

    public BNO055IMU imu;

    public DcMotorEx dcParallelDeadWheel1;
    public DcMotorEx dcParallelDeadWheel2;
    public DcMotorEx dcPerpendicularDeadWheel;

    public Camera camera;
    public boolean usingOdo;

    public NexusMecanumDrive(MecanumConstants constants, HardwareMap hardwareMap, boolean odo) {
        super(constants);
        this.usingOdo = odo;
        leftBack = hardwareMap.get(DcMotor.class, "backLeft");
        leftFront = hardwareMap.get(DcMotor.class, "frontLeft");
        rightBack = hardwareMap.get(DcMotor.class, "backRight");
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront = hardwareMap.get(DcMotor.class, "frontRight");
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        imu.initialize(parameters);
        if(odo){
            dcParallelDeadWheel1 = hardwareMap.get(DcMotorEx.class, "dcParallelDeadWheel1");
            dcParallelDeadWheel2 = hardwareMap.get(DcMotorEx.class, "dcParallelDeadWheel2");
            dcPerpendicularDeadWheel = hardwareMap.get(DcMotorEx.class, "dcPerpendicularDeadWheel");
            dcParallelDeadWheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            dcParallelDeadWheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            dcPerpendicularDeadWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            camera = hardwareMap.get(Camera.class, "camera");
            registerPart("camera", (HardwareDevice) camera);
        }

    }

    @Override
    public DcMotor getBackLeft() {
        return leftBack;
    }

    @Override
    public DcMotor getBackRight() {
        return rightBack;
    }

    @Override
    public DcMotor getFrontLeft() {
        return leftFront;
    }

    @Override
    public DcMotor getFrontRight() {
        return rightFront;
    }

    @Override
    public BNO055IMU getIMU() {
        return imu;
    }

    @Override
    public DcMotorEx getOdoParallel1() {
        return dcParallelDeadWheel1;
    }

    @Override
    public DcMotorEx getOdoParallel2() {
        return dcParallelDeadWheel2;
    }

    @Override
    public DcMotorEx getOdoPerp() {
        return dcPerpendicularDeadWheel;
    }

    @Override
    public HardwareDevice getCustomPartCast(Class<? extends HardwareDevice> partClass, String part){
        return partClass.cast(customParts.get(part));
    }

    @Override
    public HardwareDevice getCustomPart(String part){
        return customParts.get(part);
    }
}
