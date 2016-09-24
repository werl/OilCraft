package me.werl.oilcraft.inventory.slot;

import me.werl.oilcraft.util.FluidUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotFluid extends Slot {
    public SlotFluid(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(@Nullable ItemStack stack)
    {
        return FluidUtil.isFluidContainer(stack);
    }
}
