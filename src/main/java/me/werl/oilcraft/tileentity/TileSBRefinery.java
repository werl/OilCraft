package me.werl.oilcraft.tileentity;

import me.werl.oilcraft.custom_recipes.RefineryRecipe;
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

import java.util.ArrayList;
import java.util.List;

public class TileSBRefinery extends TileHeatGenerator implements IIoConfigurable, ITankUpdate {

    private int mbPerCycle = 10;

    private IoModeWrapper ioModeWrapper;

    public FilteredTank inputTank = new FilteredTank("input", 16 * Fluid.BUCKET_VOLUME, this);
    public FilteredTank outputTank = new FilteredTank("output", 16 * Fluid.BUCKET_VOLUME, this);

    private double workTemp = 400;

    private RefineryRecipe currentRecipe;
    private List<RefineryRecipe> possableRecipes = new ArrayList<>();
    private boolean needPlayerInput = false;
    private boolean autoRecipe = true;

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

            if(tryDrainItem(1, 2, inputTank)){
                tankDirty = true;
            }
            if(tryFillItem(3, 4, outputTank)) {
                tankDirty = true;
            }

            if(autoRecipe && currentRecipe == null) {
                currentRecipe = testRecipe(inputTank);
            }

            if(currentRecipe != null) {
                if(currentRecipe.isInTempRange(temperature)) {
                    if(inputTank.drainInternal(currentRecipe.getInput(), false) != null
                            && inputTank.drainInternal(currentRecipe.getInput(), false).amount == currentRecipe.getInputAmount()
                            && outputTank.fillInternal(currentRecipe.getOutputs()[0], false) == currentRecipe.getOutputs()[0].amount) {
                        inputTank.drainInternal(currentRecipe.getInput(), true);
                        outputTank.fillInternal(currentRecipe.getOutputs()[0], true);
                        tankDirty = true;
                    } else if(autoRecipe) {
                        currentRecipe = null;
                    }
                } else if(autoRecipe) {
                    currentRecipe = null;
                }
            }
            if(tankDirty) {
                PacketHandler.sendToAllAround(new PacketSBRTank(this), this);
            }
        }
    }
    // ITickable end

    private boolean tryDrainItem(int slotIn, int slotOut, FilteredTank tank) {
        if(inv[slotIn] == null)
            return false;
        if(inv[slotOut] == null || inv[slotOut].isItemEqual(inv[slotIn].getItem().getContainerItem(inv[slotIn]))
                && inv[slotOut].stackSize < inv[slotOut].getMaxStackSize()) {
            ItemStack container = inv[slotIn].getItem().getContainerItem(inv[slotIn]);
            if (FluidUtil.tryFluidTransfer(tank, FluidUtil.getFluidHandler(inv[slotIn]), tank.getCapacity() - tank.getFluidAmount(), true) != null) {
                if (container != null) {
                    if (inv[slotOut] == null) {
                        inv[slotOut] = container;
                    } else {
                        inv[slotOut].stackSize++;
                    }
                }
                inv[slotIn].stackSize--;
                if (inv[slotIn].stackSize == 0) {
                    inv[slotIn] = null;
                }
                return true;

            }
        }
        return false;
    }

    private boolean tryFillItem(int slotIn, int slotOut, FilteredTank tank) {
        if(inv[slotIn] != null && inv[slotIn].hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            if(FluidUtil.getFluidHandler(inv[slotIn]) != null) {
                ItemStack filled = FluidUtil.tryFillContainer(inv[slotIn], tank, tank.getFluidAmount(), null, false);
                if (filled != null) {
                    if(inv[slotOut] == null) {
                        inv[slotOut] = FluidUtil.tryFillContainer(inv[slotIn], tank, tank.getFluidAmount(), null, true);
                        inv[slotIn].stackSize --;
                        if(inv[slotIn].stackSize == 0) {
                            inv[slotIn] = null;
                        }
                        return true;
                    } else if(inv[slotOut].stackSize < inv[slotOut].getItem().getItemStackLimit(inv[slotOut])) {
                        FluidUtil.tryFillContainer(inv[slotIn], tank, tank.getFluidAmount(), null, true);
                        inv[slotIn].stackSize --;
                        if(inv[slotIn].stackSize == 0) {
                            inv[slotIn] = null;
                        }
                        inv[slotOut].stackSize ++;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private RefineryRecipe testRecipe(FilteredTank inTank) {
        List<RefineryRecipe> matching = new ArrayList<>();

        for(RefineryRecipe rec : RefineryRecipe.RECIPES) {
            if(inTank.drainInternal(rec.getInput(), false) != null && rec.isInTempRange(temperature)) {
                matching.add(rec);
            }
        }

        if(matching.size() == 1) {
            return matching.get(0);
        } else if(matching.size() > 1) {
            needPlayerInput = true;
            possableRecipes = matching;
            return null;
        } else {
            return null;
        }
    }

    public void setRecipe(RefineryRecipe recipe) {
        this.currentRecipe = recipe;
    }

    // NBT start
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        autoRecipe = tag.getBoolean("auto_recipe");
        needPlayerInput = tag.getBoolean("need_player_input");

        ioModeWrapper.readFromNBT(tag);

        inputTank.readFromNBT(tag);
        outputTank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        inputTank.writeToNBT(tag);
        outputTank.writeToNBT(tag);

        ioModeWrapper.writeToNBT(tag);

        tag.setBoolean("auto_recipe", autoRecipe);
        tag.setBoolean("need_player_input", needPlayerInput);

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
