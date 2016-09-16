package me.werl.oilcraft.proxy;

/**
 * Created by PeterLewis on 2016-08-28.
 */
public interface IOilProxy {

    void preInit();

    void init();

    void postInit();

    void renderingInit();
}
