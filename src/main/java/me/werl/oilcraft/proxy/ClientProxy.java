package me.werl.oilcraft.proxy;

import me.werl.oilcraft.client.ClientEvents;
import me.werl.oilcraft.client.model.ModModelManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    }

}
