package me.werl.oilcraft.tileentity.interfaces;

import me.werl.oilcraft.util.EnumIoMode;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IIoConfigurable {

    EnumIoMode toggleModeForFace(EnumFacing face);

    boolean supportsMode(EnumFacing face, EnumIoMode mode);

    void setIoMode(EnumFacing facing, EnumIoMode mode);

    EnumIoMode getMode(EnumFacing face);

    void clearAllModes();

    BlockPos getLocation();

}
