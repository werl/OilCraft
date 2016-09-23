package me.werl.oilcraft.data;

import net.minecraft.util.IStringSerializable;

public enum EnumMachines implements IStringSerializable {

    SB_REFINERY("sb_refinery");

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
