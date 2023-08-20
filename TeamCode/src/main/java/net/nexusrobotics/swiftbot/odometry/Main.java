package net.nexusrobotics.swiftbot.odometry;

import net.nexusrobotics.swiftbot.localization.LocalizationSim;

public class Main {
    public static void main(String[] args){
        new OdometrySim(new LocalizationSim(10, 10, 0, 0, 0));
    }
}
