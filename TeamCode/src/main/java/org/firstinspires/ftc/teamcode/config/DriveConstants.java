package org.firstinspires.ftc.teamcode.config;

import net.nexusrobotics.swiftbot.constants.MecanumConstants;

public class DriveConstants extends MecanumConstants {
    static float deadwheeldiameter = 3;
    static int ticks_per_rotation = 360;
    static int odoperptocenter = 6;
    static int deadWheelDistance = 10;
    public DriveConstants() {
        super(deadwheeldiameter, ticks_per_rotation, odoperptocenter, deadWheelDistance);
    }
}
