package me.werl.oilcraft.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;


public class FluidUtil {

    public static boolean isFluidContainer(ItemStack stack) {
        Item item = stack.getItem();
        for (EnumFacing facing : EnumFacing.values()) {
            if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
                return true;
            }
        }
        return item instanceof IFluidHandler;
    }

    public static boolean notFluidContainer(ItemStack stack) {
        return !isFluidContainer(stack);
    }

}
