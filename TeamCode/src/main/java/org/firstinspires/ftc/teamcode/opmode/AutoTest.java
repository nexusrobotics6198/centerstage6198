package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import net.nexusrobotics.swiftbot.drive.MecanumDrive;
import net.nexusrobotics.swiftbot.localization.Localization;
import net.nexusrobotics.swiftbot.odometry.Odometry;
import net.nexusrobotics.swiftbot.odometry.OdometryWithoutOdometry;
import net.nexusrobotics.swiftbot.robot.MecanumRobot;

import org.firstinspires.ftc.teamcode.config.DriveConstants;
import org.firstinspires.ftc.teamcode.config.NexusMecanumDrive;

@Autonomous
public class AutoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("Waiting for start.");
        waitForStart();
        MecanumRobot robot = new NexusMecanumDrive(new DriveConstants(), hardwareMap, false);
        MecanumDrive drive = new MecanumDrive(new OdometryWithoutOdometry(robot, new Localization(10, 10, 0, 0, 0)), robot);
        waitForStart();
        drive.init();
        /*drive.updateTarget(10, 10);
        robot.getFrontRight().setPower(1);*/
        robot.getFrontRight().setPower(0.1);
        robot.getBackRight().setPower(0.1);
        robot.getFrontLeft().setPower(0.1);
        robot.getBackLeft().setPower(0.1);
        while(!isStopRequested() && isStarted()){}
    }
}
