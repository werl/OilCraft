package me.werl.oilcraft;

import me.werl.oilcraft.config.Config;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.init.*;
import me.werl.oilcraft.network.GuiHandler;
import me.werl.oilcraft.network.PacketHandler;
import me.werl.oilcraft.network.PacketSBRTank;
import me.werl.oilcraft.proxy.IOilProxy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModData.ID, name = ModData.NAME, version = ModData.VERSION, dependencies = ModData.DEPS, guiFactory = ModData.GUI_FACTORY)
public class OilCraft {

    @Mod.Instance(ModData.ID)
    public static OilCraft instance;

    @SidedProxy(clientSide = ModData.CLIENT_PROXY, serverSide = ModData.SERVER_PROXY)
    public static IOilProxy proxy;

    public Logger logger;

    public static CreativeTabOilCraft creativeTab = new CreativeTabOilCraft();

    static {
        FluidRegistry.enableUniversalBucket(); // Must be called before preInit
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.logger = event.getModLog();

        Config.load(event);

        ModBlocks.registerTileEntities();
        ModFluids.registerFluids();
        ModFluids.registerFluidContainers();
        ModRecipes.registerRecipes();

        PacketHandler.INSTANCE.registerMessage(PacketSBRTank.class, PacketSBRTank.class, PacketHandler.nextID(), Side.CLIENT);

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
