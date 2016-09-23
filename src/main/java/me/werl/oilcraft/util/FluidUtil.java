package me.werl.oilcraft.util;

import me.werl.oilcraft.fluids.tanks.FilteredTank;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FluidUtil {

    public static boolean isFluidContainer(ICapabilityProvider provider) {
        return ItemStackUtil.hasCapabilityAnySide(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, provider);
    }

    public static List<IFluidTankProperties> getFluidTankPropsFromContainer(ICapabilityProvider provider) {
        if(!isFluidContainer(provider))
            return null;
        IFluidHandler handler = ItemStackUtil.getCapabilityAnySide(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, provider);
        if(handler == null)
            return null;
        IFluidTankProperties[] props = handler.getTankProperties();

        List<IFluidTankProperties> propsList = new ArrayList<>();
        Collections.addAll(propsList, props);
        return propsList;
    }

    public static int fillTankFromContainer(IFluidTank tank, ICapabilityProvider provider, boolean doFill) {
        if(isFluidContainer(provider)){
            List<IFluidTankProperties> props = getFluidTankPropsFromContainer(provider);
            if(props == null)
                return 0;
            for(IFluidTankProperties prop : props) {
                if(tank.fill(prop.getContents(), false) == prop.getCapacity()) {
                    return tank.fill(prop.getContents(), doFill);
                }
            }
        }
        return 0;
    }

    public static boolean isFluidInContainer(FluidStack test, ICapabilityProvider provider) {
        if(isFluidContainer(provider)) {
            if(test == null)
                return true;

            List<IFluidTankProperties> props = getFluidTankPropsFromContainer(provider);
            if(props == null)
                return false;

            for(IFluidTankProperties prop : props) {
                if (test.isFluidEqual(prop.getContents()))
                    return true;
            }
        }
        return false;
    }

}
