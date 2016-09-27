package me.werl.oilcraft.proxy;

import me.werl.oilcraft.client.ClientEvents;
import me.werl.oilcraft.client.model.ModModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IOilProxy {

    private final Minecraft minecraft = Minecraft.getMinecraft();

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

    @Nullable
    @Override
    public EntityPlayer getClientPlayer() {
        return minecraft.thePlayer;
    }

}
