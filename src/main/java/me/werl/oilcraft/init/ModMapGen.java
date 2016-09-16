package me.werl.oilcraft.init;

import me.werl.oilcraft.world.gen.WorldGenOil;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModMapGen {

    public static void registerWorldGenerators() {
        GameRegistry.registerWorldGenerator(new WorldGenOil(), 10);
    }

}
