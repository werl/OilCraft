package me.werl.oilcraft.data;

import me.werl.oilcraft.util.IVariant;

public enum EnumMachines implements IVariant {

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

    @Override
    public int getMeta() {
        return this.ordinal();
    }

}
