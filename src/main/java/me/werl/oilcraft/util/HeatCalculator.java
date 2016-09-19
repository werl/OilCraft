package me.werl.oilcraft.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeatCalculator {

    public static double HEAT_STEP = 0.05;

    public static double generateHeat(double temp, double max) {
        if(temp == max)
            return temp;
        double change = HEAT_STEP + (((max - temp) / max) * HEAT_STEP * 3);
        //change /= numTanks;
        temp += change;
        temp = Math.min(temp, max);
        return temp;
    }

    public static double reduceHeat(double temp, double min, double max) {
        if(temp == min)
            return temp;
        double change = HEAT_STEP + ((temp / max) * HEAT_STEP * 3);
        //change /= numTanks;
        temp -= change;
        temp = Math.max(temp, min);

        return temp;
    }

    public static float getTempForBiome(World world, BlockPos pos) {
        return 20 * world.getBiomeGenForCoords(pos).getTemperature();
    }

}
