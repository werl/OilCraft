package me.werl.oilcraft.proxy;

import me.werl.oilcraft.client.ClientEvents;
import me.werl.oilcraft.client.model.ModModelManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by PeterLewis on 2016-08-28.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy implements IOilProxy {

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);

        this.renderingInit();
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @Override
    public void renderingInit() {
        ModModelManager.INSTANCE.registerAllModels();
    }

}
