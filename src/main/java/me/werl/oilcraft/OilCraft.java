package me.werl.oilcraft;

import me.werl.oilcraft.config.Config;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.init.ModBlocks;
import me.werl.oilcraft.init.ModFluids;
import me.werl.oilcraft.init.ModItems;
import me.werl.oilcraft.init.ModMapGen;
import me.werl.oilcraft.network.GuiHandler;
import me.werl.oilcraft.proxy.IOilProxy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModData.ID, name = ModData.NAME, version = ModData.VERSION, dependencies = ModData.DEPS, guiFactory = ModData.GUI_FACTORY)
public class OilCraft {

    @Mod.Instance(ModData.ID)
    public static OilCraft instance;

    @SidedProxy(clientSide = ModData.CLIENT_PROXY, serverSide = ModData.SERVER_PROXY)
    public static IOilProxy proxy;

    public Logger logger;

    public static CreativeTabOilCraft creativeTab;

    static {
        FluidRegistry.enableUniversalBucket(); // Must be called before preInit
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.logger = event.getModLog();

        creativeTab = new CreativeTabOilCraft();

        Config.load(event);

        ModBlocks.registerBlocks();
        ModBlocks.registerTileEntities();
        ModItems.registerItems();
        ModFluids.registerFluids();
        ModFluids.registerFluidContainers();

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModMapGen.registerWorldGenerators();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {


        proxy.postInit();
    }

}
