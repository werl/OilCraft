package me.werl.oilcraft.tileentity;

import me.werl.oilcraft.blocks.BlockMachine;
import me.werl.oilcraft.tileentity.interfaces.IActivatableTile;
import me.werl.oilcraft.tileentity.interfaces.IFacingTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public abstract class TileMachine extends TileInventory implements ITickable, IActivatableTile, IFacingTile {

    protected boolean futureActive;
    protected boolean isActive;

    protected EnumFacing facing;

    protected TileMachine(int slots) {
        super(slots);
    }

    // ITickable start
    @Override
    public void update() {
        if (isActive != futureActive) {
            BlockMachine.setState(futureActive, facing, worldObj, pos);
            isActive = futureActive;
        }
    }
    // ITickable end

    protected void changeActivity(boolean active) {
        futureActive = active;
    }

    // IActivatableTile start
    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }
    // IActivatableTile end

    // IFacingTile start
    @Override
    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }

    @Override
    public EnumFacing getFacing() {
        return facing;
    }
    // IFacingTile end

    //NBT start
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.facing = EnumFacing.byName(tag.getString("facing"));
        if(facing == null)
            facing = EnumFacing.NORTH;
        this.isActive = tag.getBoolean("is_active");
        this.futureActive = tag.getBoolean("future_active");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        // Just in case the world saves before facing is set
        if(facing == null)
            facing = EnumFacing.NORTH;
        tag.setString("facing", facing.getName());
        tag.setBoolean("is_active", isActive);
        tag.setBoolean("future_active", futureActive);

        return super.writeToNBT(tag);
    }
    //NBT end

}
