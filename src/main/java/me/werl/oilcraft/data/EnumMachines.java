package me.werl.oilcraft.data;

import net.minecraft.util.IStringSerializable;

/**
 * Created by PeterLewis on 2016-09-05.
 */
public enum EnumMachines implements IStringSerializable {

    HEAT_GENERATOR("heat_generator");

    private final String name;

    EnumMachines(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
