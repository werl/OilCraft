package me.werl.oilcraft.tileentity;

import me.werl.oilcraft.fluids.tanks.FilteredTank;
import me.werl.oilcraft.util.FluidUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;


import java.util.ArrayList;
import java.util.List;

public class TileSBRefinery extends TileHeatGenerator {

    private List<EnumFacing> outputFaces = new ArrayList<>();
    private List<EnumFacing> inputFaces = new ArrayList<>();

    private FilteredTank inputTank = new FilteredTank("input", 16 * Fluid.BUCKET_VOLUME, this);
    private FilteredTank outputTank = new FilteredTank("output", 16 * Fluid.BUCKET_VOLUME, this);

    public TileSBRefinery() {
        super(5, 0);
    }

    // ITickable
    @Override
    public void update() {
        super.update();
    }

    // NBT start
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        inputTank.readFromNBT(tag);
        outputTank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        inputTank.writeToNBT(tag);
        outputTank.writeToNBT(tag);

        return super.writeToNBT(tag);
    }
    // NBT end

    // IInventory start
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if(index == 2 || index == 4)
            return false;
        if(index == 1 || index == 3) {
            return FluidUtil.isFluidContainer(stack);
        }
        return super.isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return super.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        super.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
    // IInventory end

    // Capability
    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {

        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if(outputFaces.contains(facing))
                return (T) outputTank;
            else if(inputFaces.contains(facing))
                return (T) inputTank;
        }


        return super.getCapability(capability, facing);
    }
}
