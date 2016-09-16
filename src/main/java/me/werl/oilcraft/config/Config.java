package me.werl.oilcraft.config;

import me.werl.oilcraft.data.ModData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {

    static final String LANG_PREFIX = ModData.ID + ".config.";

    static Configuration config;

    public static double oilGenLakeChance;
    public static double oilGenGeyserChance;

    public static void load(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());

        MinecraftForge.EVENT_BUS.register(Config.class);
    }
    
    private static void reloadConfig() {
        oilGenLakeChance = config.getFloat("oilGenLakeChance", Configuration.CATEGORY_GENERAL, 15f, 0f, 50f,
                "How likely oil lakes are to spawn", LANG_PREFIX + "oil_lake");
        oilGenGeyserChance = config.getFloat("oilGenGeyserChance", Configuration.CATEGORY_GENERAL, 1f, 0f, 50f,
                "How likely oil geysers are to spawn", LANG_PREFIX + "oil_geyser");
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ModData.ID))
            reloadConfig();
    }

}
