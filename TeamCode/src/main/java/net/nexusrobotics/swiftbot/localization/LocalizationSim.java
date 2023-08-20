package net.nexusrobotics.swiftbot.localization;

public class LocalizationSim {
    public float currentX;
    public float currentY;
    public float robotWidth;
    public float robotHeight;
    public float currentHeading;

    public LocalizationSim(float robotWidth, float robotHeight, float startX, float startY, float startHeading){
        this.robotHeight = robotHeight;
        this.robotWidth = robotWidth;
        this.currentX = startX;
        this.currentY = startY;
        this.currentHeading = startHeading;
    }

    public void setX(float x){
        this.currentX = x;
    }

    public void setY(float y){
        this.currentY = y;
    }

    public void setHeading(float h){
        this.currentHeading = h;
    }
}
