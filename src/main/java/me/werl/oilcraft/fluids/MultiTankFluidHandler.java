package me.werl.oilcraft.fluids;

import me.werl.oilcraft.fluids.tanks.FilteredTank;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class MultiTankFluidHandler {

    protected final IFluidHandler[] tanks;
    private final SideHandler[] sides = new SideHandler[EnumFacing.values().length];

    public MultiTankFluidHandler(IFluidHandler... tanks) {
        this.tanks = tanks;
    }

    public boolean has(EnumFacing facing) {
        return facing != null && canAccess(facing);
    }

    public SideHandler get(EnumFacing facing) {
        if (has(facing)) {
            if (sides[facing.ordinal()] == null) {
                sides[facing.ordinal()] = new SideHandler(facing);
            }
            return sides[facing.ordinal()];
        } else {
            return null;
        }
    }

    protected abstract boolean canFill(EnumFacing from);

    protected abstract boolean canDrain(EnumFacing from);

    protected abstract boolean canAccess(EnumFacing from);

    private class SideHandler implements IFluidHandler {
        private final EnumFacing facing;

        public SideHandler(EnumFacing facing) {
            this.facing = facing;
        }


        @Override
        public IFluidTankProperties[] getTankProperties() {
            if (!canAccess(facing)) {
                return new IFluidTankProperties[0];
            }
            if (tanks.length == 1) {
                return tanks[0].getTankProperties();
            }
            List<IFluidTankProperties> result = new ArrayList<>();
            for (IFluidHandler smartTank : tanks) {
                IFluidTankProperties[] tankProperties = smartTank.getTankProperties();
                if (tankProperties != null) {
                    for (IFluidTankProperties tankProperty : tankProperties) {
                        result.add(tankProperty);
                    }
                }
            }
            return result.toArray(new IFluidTankProperties[result.size()]);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (!canFill(facing)) {
                return 0;
            }
            if (tanks.length == 1) {
                return tanks[0].fill(resource, doFill);
            }
            for (IFluidHandler smartTank : tanks) {
                if (smartTank instanceof FilteredTank) {
                    if (((FilteredTank) smartTank).canFill(resource)) {
                        return smartTank.fill(resource, doFill);
                    }
                } else if (smartTank instanceof FluidTank) {
                    if (((FluidTank) smartTank).canFill()) {
                        return smartTank.fill(resource, doFill);
                    }
                } else {
                    return smartTank.fill(resource, doFill);
                }
            }
            return 0;
        }

        @Override
        @Nullable
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (!canDrain(facing)) {
                return null;
            }
            if (tanks.length == 1) {
                return tanks[0].drain(resource, doDrain);
            }
            for (IFluidHandler smartTank : tanks) {
                if (!(smartTank instanceof FluidTank) || ((FluidTank) smartTank).canDrainFluidType(resource)) {
                    return smartTank.drain(resource, doDrain);
                }
            }
            return null;
        }

        @Override
        @Nullable
        public FluidStack drain(int maxDrain, boolean doDrain) {
            if (!canDrain(facing)) {
                return null;
            }
            if (tanks.length == 1) {
                return tanks[0].drain(maxDrain, doDrain);
            }
            for (IFluidHandler smartTank : tanks) {
                if (!(smartTank instanceof FluidTank) || ((FluidTank) smartTank).canDrain()) {
                    return smartTank.drain(maxDrain, doDrain);
                }
            }
            return null;
        }

    }

}