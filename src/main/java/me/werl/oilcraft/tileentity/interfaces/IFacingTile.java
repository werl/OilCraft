package me.werl.oilcraft.tileentity.interfaces;

import net.minecraft.util.EnumFacing;

public interface IFacingTile {

    void setFacing(EnumFacing facing);

    EnumFacing getFacing();

}
