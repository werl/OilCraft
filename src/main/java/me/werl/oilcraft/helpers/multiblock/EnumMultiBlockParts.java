package me.werl.oilcraft.helpers.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by PeterLewis on 2016-09-01.
 */
public enum EnumMultiBlockParts implements INBTSerializable<NBTTagString> {

    WALL("refinery.wall"),
    FRAME("refinery.frame"),
    FILL("refinery.fill");

    private String tag;

    EnumMultiBlockParts(String tag) {
        this.tag = tag;
    }

    @Override
    public NBTTagString serializeNBT() {
        return new NBTTagString(tag);
    }

    @Override
    public void deserializeNBT(NBTTagString nbt) {
        this.tag = nbt.getString();
    }

}
