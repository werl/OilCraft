package me.werl.oilcraft.proxy;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public interface IOilProxy {

    void preInit();

    void init();

    void postInit();

    void renderingInit();

    @Nullable
    EntityPlayer getClientPlayer();
}
