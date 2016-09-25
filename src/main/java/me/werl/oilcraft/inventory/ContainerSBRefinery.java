package me.werl.oilcraft.inventory;

import me.werl.oilcraft.data.FluidData;
import me.werl.oilcraft.inventory.slot.SlotFluid;
import me.werl.oilcraft.inventory.slot.SlotOutput;
import me.werl.oilcraft.util.FuelUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ContainerSBRefinery extends Container {

    private final IInventory refinery;
    private int burnTime;
    private int currentItemBurnTime;
    private int heat;
    private int maxHeat;


    public ContainerSBRefinery(InventoryPlayer playerInventory, IInventory refinery) {
        this.refinery = refinery;

        this.addSlotToContainer(new SlotFurnaceFuel(refinery, 0, 13, 39));
        this.addSlotToContainer(new SlotFluid(refinery, 1, 56, 19));
        this.addSlotToContainer(new SlotOutput(refinery, 2, 56, 51));
        this.addSlotToContainer(new SlotFluid(refinery, 3, 104, 19));
        this.addSlotToContainer(new SlotOutput(refinery, 4, 104, 51));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

    }

    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.refinery);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.listeners.size(); i++) {
            IContainerListener listener = this.listeners.get(i);

            if(this.burnTime != this.refinery.getField(0)) {
                listener.sendProgressBarUpdate(this, 0, this.refinery.getField(0));
            }
            if(this.currentItemBurnTime != this.refinery.getField(1)) {
                listener.sendProgressBarUpdate(this, 1, this.refinery.getField(1));
            }
            if(this.heat != this.refinery.getField(2)) {
                listener.sendProgressBarUpdate(this, 2, this.refinery.getField(2));
            }
            if(this.maxHeat != this.refinery.getField(3)) {
                listener.sendProgressBarUpdate(this, 3, this.refinery.getField(3));
            }
        }

        this.burnTime = this.refinery.getField(0);
        this.currentItemBurnTime = this.refinery.getField(1);
        this.heat = this.refinery.getField(2);
        this.maxHeat = this.refinery.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.refinery.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return refinery.isUseableByPlayer(playerIn);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = null;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if(index == 0) {
                if(!this.mergeItemStack(stackInSlot, 5, 41, false)) {
                    return null;
                }
            } else if(index >= 1 && index < 5) {
                if(!this.mergeItemStack(stackInSlot, 5, 41, false)) {
                    return null;
                }
            } else {
                    if (FuelUtil.isBurnableInBoiler(stackInSlot)) {
                        if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
                            return null;
                        }
                    } else if(stackInSlot.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                        if(FluidUtil.getFluidContained(stackInSlot) != null && FluidUtil.getFluidContained(stackInSlot).amount > 0) {
                            if (!this.mergeItemStack(stackInSlot, 1, 2, false)) {
                                return null;
                            }
                        }
                    } else if(stackInSlot.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                        if(FluidUtil.getFluidContained(stackInSlot) == null || FluidUtil.getFluidContained(stackInSlot).amount == 0) {
                            if (!this.mergeItemStack(stackInSlot, 3, 4, false)) {
                                return null;
                            }
                        }
                    }else if (index >= 5 && index < 32) {
                        if (!this.mergeItemStack(stackInSlot, 32, 41, false)) {
                            return null;
                        }
                    } else if (index >= 32 && index < 41 && !this.mergeItemStack(stackInSlot, 5, 32, false)) {
                        return null;
                    }
                }
            if (stackInSlot.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if(stackInSlot.stackSize == stack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(playerIn, stackInSlot);
        }

        return stack;
    }
}
