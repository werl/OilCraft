package me.werl.oilcraft.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by PeterLewis on 2016-09-10.
 */
public class TemperatureUtil {

    public static float getTempForBiome(World world, BlockPos pos) {
        return 20 * world.getBiomeGenForCoords(pos).getTemperature();
    }

}