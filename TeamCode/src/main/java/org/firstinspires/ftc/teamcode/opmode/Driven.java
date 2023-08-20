package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import net.nexusrobotics.swiftbot.constants.MecanumConstants;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.config.NexusMecanumDrive;

@TeleOp
public class Driven extends OpMode {
    private NexusMecanumDrive robot;
    double robotX = 0;
    double robotY = 0;

    // Reset the encoders before starting
    @Override
    public void init(){

        robot = new NexusMecanumDrive(new MecanumConstants(2, 360, 6, 10), hardwareMap, false);

    }
    @Override
    public void loop(){
        float heading = robot.getIMU().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, BNO055IMU.AngleUnit.DEGREES.toAngleUnit()).firstAngle;
        if(heading < 0){
            heading = 180 + (180 - Math.abs(robot.getIMU().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, BNO055IMU.AngleUnit.DEGREES.toAngleUnit()).firstAngle));
        }
        telemetry.addData("Rotation", heading);
        double drive = -gamepad1.left_stick_y/3.5;
        double tmp = drive;
        double strafe = (gamepad1.left_stick_x * 1.1)/4.5;
        double turn = (gamepad1.right_stick_x)/4.9;
        drive = convertPower(heading, (float)drive, (float)strafe)[1];
        strafe = convertPower(heading, (float)tmp, (float)strafe)[0];
        telemetry.addData("Drive", drive);
        telemetry.addData("Strafe", strafe);
        double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 0.65);
        double frontLeftPower = (drive + strafe + turn) / denominator;
        double backLeftPower = (drive - strafe + turn) / denominator;
        double frontRightPower = (drive - strafe - turn) / denominator;
        double backRightPower = (drive + strafe - turn) / denominator;

        robot.leftFront.setPower(frontLeftPower);
        robot.rightFront.setPower(frontRightPower);
        robot.leftBack.setPower(backLeftPower);
        robot.rightBack.setPower(backRightPower);
    }
    static double[] convertPower(double angleInDegrees, double y, double x) {
        // Convert angle to radians
        double angleInRadians = Math.toRadians(360 - angleInDegrees);

        // Calculate new coordinates after rotation
        double newX = x * Math.cos(angleInRadians) - y * Math.sin(angleInRadians);
        double newY = x * Math.sin(angleInRadians) + y * Math.cos(angleInRadians);

        double[] rotatedPoint = {newX, newY};
        return rotatedPoint;
    }
}
