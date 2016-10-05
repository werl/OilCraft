package me.werl.oilcraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileBase extends TileEntity {

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tagCompound = new NBTTagCompound();

        tagCompound.setInteger("x", pos.getX());
        tagCompound.setInteger("y", pos.getY());
        tagCompound.setInteger("z", pos.getZ());

        return writeToNBT(tagCompound);
    }

}
