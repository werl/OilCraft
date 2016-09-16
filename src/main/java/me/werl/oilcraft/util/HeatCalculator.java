package me.werl.oilcraft.util;

public class HeatCalculator {

    private static double lowerBound = -200;
    private static double upperBound = 1000;
    private static double length = 9; //0.00008
    private static double height = 0.5; //0.00160095
    private static double correction = 25;
    private static double offset = (-(lowerBound - 1)) + correction;

    public static double getTempIncrease(double currentTemp) {

        if(currentTemp < lowerBound){
            currentTemp = lowerBound;
        }
        if(currentTemp > upperBound){
            return -(currentTemp - upperBound);
        }
        double tempDelta = deltaTemp(currentTemp);
        if(tempDelta + currentTemp > upperBound){
            return upperBound;
        }
        return tempDelta;
    }

    public static double getTempDecrease(double currentTemp) {

        if(currentTemp > upperBound){
            currentTemp = upperBound;
        }
        if(currentTemp < lowerBound){
            return -(currentTemp - upperBound);
        }
        double tempDelta =deltaTemp(currentTemp);
        if(currentTemp - tempDelta < lowerBound){
            return lowerBound;
        }
        return -tempDelta;
    }

    private static double deltaTemp(double currentTemp) {
        return (height * (currentTemp + offset)) / (length * (currentTemp + offset) * (currentTemp + offset));
    }

}
