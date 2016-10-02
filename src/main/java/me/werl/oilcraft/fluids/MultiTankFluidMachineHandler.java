package me.werl.oilcraft.fluids;

import me.werl.oilcraft.tileentity.interfaces.IIoConfigurable;
import me.werl.oilcraft.util.EnumIoMode;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class MultiTankFluidMachineHandler extends MultiTankFluidHandler {

    private final IIoConfigurable te;

    public MultiTankFluidMachineHandler(IIoConfigurable te, IFluidHandler... tanks) {
        super(tanks);
        this.te = te;
    }

    @Override
    protected boolean canFill(EnumFacing from) {
        EnumIoMode mode = te.getMode(from);
        return mode != EnumIoMode.PUSH && mode != EnumIoMode.EXTRACT && mode != EnumIoMode.DISABLED;
    }

    @Override
    protected boolean canDrain(EnumFacing from) {
        EnumIoMode mode = te.getMode(from);
        return mode != EnumIoMode.PULL && mode != EnumIoMode.INSERT && mode != EnumIoMode.DISABLED;
    }

    @Override
    protected boolean canAccess(EnumFacing from) {
        EnumIoMode mode = te.getMode(from);
        return mode != EnumIoMode.DISABLED;
    }

}
