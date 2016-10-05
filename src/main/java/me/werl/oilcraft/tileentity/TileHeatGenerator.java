package me.werl.oilcraft.tileentity;

import me.werl.oilcraft.blocks.BlockMachine;
import me.werl.oilcraft.tileentity.interfaces.IActivatableTile;
import me.werl.oilcraft.tileentity.interfaces.IFacingTile;
import me.werl.oilcraft.util.FuelUtil;
import me.werl.oilcraft.util.HeatCalculator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;


public abstract class TileHeatGenerator extends TileMachine implements ITickable {

    protected boolean firstTick = true;

    protected int burnTime;
    protected int currentItemBurnTime;
    protected double temperature;
    protected double startTemp;
    protected double maxTemperature;

    protected int fuelSlot;

    protected TileHeatGenerator(int inventorySize, int fuelSlot) {
        super(inventorySize);
        this.fuelSlot = fuelSlot;
    }

    public boolean isBurning() {
        return this.burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    private void generateHeat() {
        temperature = HeatCalculator.generateHeat(temperature, maxTemperature);
    }

    private void reduceHeat() {
        temperature = HeatCalculator.reduceHeat(temperature, this.startTemp, maxTemperature);
    }

    // ITickable
    @Override
    public void update() {
        boolean currentActive = isBurning();
        boolean makeDirty = false;

        if(this.isBurning())
            --this.burnTime;
        if(!this.worldObj.isRemote) {
            if (this.firstTick) {
                this.maxTemperature = 1000;
                this.temperature = HeatCalculator.getTempForBiome(this.worldObj, this.pos);
                this.startTemp = temperature;
                firstTick = false;
            }

            if (this.isBurning()) {
                this.generateHeat();
            } else if (FuelUtil.isBurnableInBoiler(this.inv[0])) {
                this.burnTime = FuelUtil.getSolidBurnTimeBoiler(this.inv[0]);
                this.currentItemBurnTime = FuelUtil.getSolidBurnTimeBoiler(this.inv[0]);
                --this.inv[0].stackSize;
                if (this.inv[0].stackSize == 0)
                    this.inv[0] = null;
                this.generateHeat();
                makeDirty = true;
            } else {
                reduceHeat();
                makeDirty = true;
            }
            if (currentActive != isBurning()) {
                makeDirty = true;
                changeActivity(isBurning());
            }
        }

        if(makeDirty) {
            this.markDirty();
        }
        super.update();
    }

    // NBT start
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.burnTime = tag.getInteger("burn_time");
        this.currentItemBurnTime = tag.getInteger("current_item_burn_time");
        this.temperature = tag.getDouble("temperature");
        this.startTemp = tag.getDouble("start_temp");
        this.maxTemperature = tag.getDouble("max_temperature");
        this.firstTick = tag.getBoolean("first_tick");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("burn_time", burnTime);
        tag.setInteger("current_item_burn_time", currentItemBurnTime);
        tag.setDouble("temperature", temperature);
        tag.setDouble("start_temp", startTemp);
        tag.setDouble("max_temperature", maxTemperature);
        tag.setBoolean("first_tick", firstTick);

        return super.writeToNBT(tag);
    }
    // NBT end

    // Network Start
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }
    // Network end

    // IInventory Start
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == fuelSlot && FuelUtil.isBurnableInBoiler(stack);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.burnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return Math.round(Math.round(this.temperature));
            case 3:
                return Math.round(Math.round(this.maxTemperature));

        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.temperature = value;
                break;
            case 3:
                this.maxTemperature = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }
    // IInventory End

    // Capability
    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) new InvWrapper(this);

        return super.getCapability(capability,facing);
    }

}
