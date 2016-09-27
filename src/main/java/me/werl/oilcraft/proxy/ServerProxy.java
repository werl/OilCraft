package me.werl.oilcraft.proxy;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class ServerProxy implements IOilProxy {

    @Override
    public void preInit() {

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
        return null;
    }

}
