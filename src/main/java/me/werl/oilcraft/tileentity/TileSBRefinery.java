package me.werl.oilcraft.tileentity;

import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.fluids.MultiTankFluidHandler;
import me.werl.oilcraft.fluids.MultiTankFluidMachineHandler;
import me.werl.oilcraft.fluids.tanks.FilteredTank;
import me.werl.oilcraft.init.ModFluids;
import me.werl.oilcraft.network.PacketHandler;
import me.werl.oilcraft.network.PacketSBRTank;
import me.werl.oilcraft.tileentity.interfaces.IIoConfigurable;
import me.werl.oilcraft.tileentity.interfaces.ITankUpdate;
import me.werl.oilcraft.util.EnumIoMode;
import me.werl.oilcraft.util.IoModeWrapper;
import me.werl.oilcraft.util.SafeTimeTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileSBRefinery extends TileHeatGenerator implements IIoConfigurable, ITankUpdate {

    private SafeTimeTracker tracker = new SafeTimeTracker(5);

    private int mbPerCycle = 10;

    private IoModeWrapper ioModeWrapper;

    public FilteredTank inputTank = new FilteredTank("input", 16 * Fluid.BUCKET_VOLUME, this);
    public FilteredTank outputTank = new FilteredTank("output", 16 * Fluid.BUCKET_VOLUME, this);

    private double workTemp = 400;

    public TileSBRefinery() {
        super(5, 0);

        inputTank.setCanDrain(false);
        outputTank.setCanFill(false);
        ioModeWrapper = new IoModeWrapper(EnumIoMode.DISABLED, EnumIoMode.DISABLED, EnumIoMode.DISABLED, EnumIoMode.DISABLED, EnumIoMode.INSERT, EnumIoMode.EXTRACT);
    }

    // ITickable start
    @Override
    public void update() {
        super.update();

        if(!worldObj.isRemote) {
            boolean tankDirty = false;

            if(canDrainItem(1, 2)) {
                ItemStack container = inv[1].getItem().getContainerItem(inv[1]);
                if(FluidUtil.tryFluidTransfer(inputTank, FluidUtil.getFluidHandler(inv[1]), inputTank.getCapacity() - inputTank.getFluidAmount(), true) != null) {
                    if(container != null) {
                        if(inv[2] == null) {
                            inv[2] = container;
                        } else {
                            inv[2].stackSize++;
                        }
                    }
                    inv[1].stackSize--;
                    if(inv[1].stackSize == 0) {
                        inv[1] = null;
                    }
                    tankDirty = true;
                }
            }
            if(inv[3] != null && inv[3].hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                if(FluidUtil.getFluidHandler(inv[3]) != null) {
                    ItemStack filled = FluidUtil.tryFillContainer(inv[3], outputTank, outputTank.getFluidAmount(), null, false);
                    if (filled != null) {
                        if(inv[4] == null) {
                            inv[4] = FluidUtil.tryFillContainer(inv[3], outputTank, outputTank.getFluidAmount(), null, true);
                            tankDirty = true;
                            inv[3].stackSize --;
                            if(inv[3].stackSize == 0) {
                                inv[3] = null;
                            }
                        } else if(inv[4].stackSize < inv[4].getItem().getItemStackLimit(inv[4])) {
                            FluidUtil.tryFillContainer(inv[3], outputTank, outputTank.getFluidAmount(), null, true);
                            inv[3].stackSize --;
                            if(inv[3].stackSize == 0) {
                                inv[3] = null;
                            }
                            inv[4].stackSize ++;
                            tankDirty = true;
                        }
                    }
                }
            }

            if(tracker.markTimeIfDelay(worldObj) && temperature >= workTemp) {
                if(inputTank.getFluid() != null && inputTank.getFluidAmount() >= mbPerCycle
                        && outputTank.getAvailableCapacity() >= mbPerCycle) {
                    inputTank.setFluid(new FluidStack(ModFluids.OIL, inputTank.getFluidAmount() - mbPerCycle));
                    outputTank.setFluid(new FluidStack(ModFluids.FUEL, mbPerCycle + outputTank.getFluidAmount()));
                    tankDirty = true;
                }
            }
            if(temperature < workTemp) {
                tracker.markTime(worldObj);
            }
            if(tankDirty) {
                PacketHandler.sendToAllAround(new PacketSBRTank(this), this);
            }
        }
    }
    // ITickable end

    private boolean canDrainItem(int in, int out) {
        if(inv[in] == null)
            return false;
        if(inv[out] == null || inv[out].isItemEqual(inv[in].getItem().getContainerItem(inv[in]))) {
            if(FluidUtil.getFluidHandler(inv[in]) != null) {
                return true;
            }
        }

        return false;
    }

    // NBT start
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        ioModeWrapper.readFromNBT(tag);

        inputTank.readFromNBT(tag);
        outputTank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        inputTank.writeToNBT(tag);
        outputTank.writeToNBT(tag);

        ioModeWrapper.writeToNBT(tag);

        return super.writeToNBT(tag);
    }
    // NBT end

    // IInventory start
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if(index == 2 || index == 4)
            return false;
        if(index == 1 || index == 3) {
            return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
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
        return super.getFieldCount();
    }

    @Override
    public String getName() {
        return ModData.RESOURCE_PREFIX + "tile.sb_refinery.name";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
    // IInventory end

    // IItemHandler start
    @Override
    public int getSlots() {
        return inv.length;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack stackInSlot = inv[slot];
        if(ItemHandlerHelper.canItemStacksStack(stackInSlot, stack)) {
            int sum = stackInSlot.stackSize + stack.stackSize;
            if(sum <= stackInSlot.getItem().getItemStackLimit(stackInSlot)) {
                if(!simulate) {
                    inv[slot].stackSize = sum;
                }
                return null;
            } else {
                if(!simulate) {
                    inv[slot].stackSize = stackInSlot.getItem().getItemStackLimit(stackInSlot);
                }
                stack.stackSize = sum - stackInSlot.getItem().getItemStackLimit(stackInSlot);
                return stack;
            }
        }
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }
    // IItemHandler end

    // IIoConfigurable start
    @Override
    public EnumIoMode toggleModeForFace(EnumFacing face) {
        ioModeWrapper.setIoMode(face, ioModeWrapper.getMode(face).getNext());
        return ioModeWrapper.getMode(face);
    }

    @Override
    public boolean supportsMode(EnumFacing face, EnumIoMode mode) {
        return ioModeWrapper.getMode(face) == mode;
    }

    @Override
    public void setIoMode(EnumFacing facing, EnumIoMode mode) {
        ioModeWrapper.setIoMode(facing, mode);
    }

    @Override
    public EnumIoMode getMode(EnumFacing face) {
        return ioModeWrapper.getMode(face);
    }

    @Override
    public void clearAllModes() {
        ioModeWrapper = new IoModeWrapper();
    }

    @Override
    public BlockPos getLocation() {
        return pos;
    }
    // IIoConfigurable end

    // ITankUpdate start
    @Override
    public void tankUpdate() {
        PacketHandler.sendToAllAround(new PacketSBRTank(this), this);
    }
    // ITankUpdate end

    // Capability helpers start
    private MultiTankFluidHandler tankHandler;

    protected MultiTankFluidHandler getMultiTankFluidHandler() {
        if(tankHandler == null) {
            tankHandler = new MultiTankFluidMachineHandler(this, inputTank, outputTank);
        }
        return tankHandler;
    }
    // Capability helpers end

    // Capability
    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {

        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return getMultiTankFluidHandler().has(facing);

        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) getMultiTankFluidHandler().get(facing);
        }

        return super.getCapability(capability, facing);
    }

}
