package me.werl.oilcraft.tileentity;

import me.werl.oilcraft.blocks.BlockMachine;
import me.werl.oilcraft.tileentity.interfaces.IActivatableTile;
import me.werl.oilcraft.tileentity.interfaces.IFacingTile;
import me.werl.oilcraft.util.FuelUtil;
import me.werl.oilcraft.util.TemperatureUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;


public class TileHeatGenerator extends TileInventory implements ITickable, IFacingTile, IActivatableTile {

    private boolean firstTick = true;

    private int burnTime;
    private int currentItemBurnTime;
    private double temperature;
    private double startTemp;
    private double maxTemperature;

    private EnumFacing facing;
    private boolean isActive;

    public TileHeatGenerator() {
        super(1);
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
        double max = maxTemperature;
        if(temperature == max)
            return;
        double step = 0.05f;
        double change = step + (((max - temperature) / max) * step * 3);
        //change /= numTanks;
        temperature += change;
        temperature = Math.min(temperature, max);
    }

    private void reduceHeat() {
        if(temperature == startTemp)
            return;
        double step = 0.05;
        double change = step + ((temperature / maxTemperature) * step * 3);
        //change /= numTanks;
        temperature -= change;
        temperature = Math.max(temperature, startTemp);

    }

    // ITickable
    @Override
    public void update() {
        boolean currentActive = isBurning();
        boolean makeDirty = false;

        if(this.isBurning())
            --this.burnTime;
        if(!this.worldObj.isRemote) {
            if(this.firstTick) {
                this.maxTemperature = 1000;
                this.temperature = TemperatureUtil.getTempForBiome(this.worldObj, this.pos);
                this.startTemp = temperature;
                firstTick = false;
            }

            if(this.isBurning()) {
                this.generateHeat();
            } else if (FuelUtil.isBurnable(this.inv[0])) {
                this.burnTime = FuelUtil.getItemBurnTime(this.inv[0]);
                this.currentItemBurnTime = FuelUtil.getItemBurnTime(this.inv[0]);
                --this.inv[0].stackSize;
                if(this.inv[0].stackSize == 0)
                    this.inv[0] = null;
                this.generateHeat();
                makeDirty = true;
            } else {
                reduceHeat();
                makeDirty = true;
            }
            if(currentActive != isBurning()) {
                makeDirty = true;
                BlockMachine.setState(this.isBurning(), facing, worldObj, pos);
                this.isActive = this.isBurning();
            }
        }
        if(makeDirty)
            this.markDirty();
    }

    // NBT stuff
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.burnTime = tag.getInteger("burn_time");
        this.currentItemBurnTime = tag.getInteger("current_item_burn_time");
        this.temperature = tag.getDouble("temperature");
        this.maxTemperature = tag.getDouble("max_temperature");
        this.firstTick = tag.getBoolean("first_tick");

        this.facing = EnumFacing.byName(tag.getString("facing"));
        if(facing == null)
            facing = EnumFacing.NORTH;
        this.isActive = tag.getBoolean("is_active");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("burn_time", burnTime);
        tag.setInteger("current_item_burn_time", currentItemBurnTime);
        tag.setDouble("temperature", temperature);
        tag.setDouble("max_temperature", maxTemperature);
        tag.setBoolean("first_tick", firstTick);

        tag.setString("facing", facing.getName());
        tag.setBoolean("is_active", isActive);

        return super.writeToNBT(tag);
    }



    // Network Stuff
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tagCompound = new NBTTagCompound();

        tagCompound.setInteger("x", pos.getX());
        tagCompound.setInteger("y", pos.getY());
        tagCompound.setInteger("z", pos.getZ());

        return writeToNBT(tagCompound);
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), writeToNBT(new NBTTagCompound()));
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }

    // IInventory Start
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return FuelUtil.isBurnable(stack);
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

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
    // IInventory End

    // IFacingTile start
    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
        if(this.facing == null)
            this.facing = EnumFacing.SOUTH;
        this.markDirty();
    }
    // IFacingTile end

    // IActivatableTile start
    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        this.isActive = active;
        this.markDirty();
    }
    // IActivatableTile end

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
