package me.werl.oilcraft.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class IoModeWrapper {

    private EnumIoMode up;
    private EnumIoMode down;
    private EnumIoMode north;
    private EnumIoMode south;
    private EnumIoMode east;
    private EnumIoMode west;

    public IoModeWrapper() {
        this(EnumIoMode.NONE, EnumIoMode.NONE, EnumIoMode.NONE, EnumIoMode.NONE, EnumIoMode.NONE, EnumIoMode.NONE);
    }

    public IoModeWrapper(EnumIoMode up, EnumIoMode down, EnumIoMode north, EnumIoMode south, EnumIoMode east, EnumIoMode west) {
        this.up = up;
        this.down = down;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagIn) {
        NBTTagCompound iTag = new NBTTagCompound();

        iTag.setString("up", up.getName());
        iTag.setString("down", down.getName());
        iTag.setString("north", north.getName());
        iTag.setString("south", south.getName());
        iTag.setString("east", east.getName());
        iTag.setString("west", west.getName());

        tagIn.setTag("IO", iTag);
        return tagIn;
    }

    public void readFromNBT(NBTTagCompound tagIn) {
        NBTTagCompound iTag = tagIn.getCompoundTag("IO");
        if(iTag == null)
            return;
        up = EnumIoMode.getModeFromString(iTag.getString("up"));
        down = EnumIoMode.getModeFromString(iTag.getString("down"));
        north = EnumIoMode.getModeFromString(iTag.getString("north"));
        south = EnumIoMode.getModeFromString(iTag.getString("south"));
        east = EnumIoMode.getModeFromString(iTag.getString("east"));
        west = EnumIoMode.getModeFromString(iTag.getString("west"));
    }

    public void setIoMode(EnumFacing face, EnumIoMode mode) {
        switch (face) {
            case UP:
                up = mode;
                break;
            case DOWN:
                down = mode;
                break;
            case NORTH:
                north = mode;
                break;
            case SOUTH:
                south = mode;
                break;
            case EAST:
                east = mode;
                break;
            case WEST:
                west = mode;
        }
    }

    public EnumIoMode getMode(EnumFacing face) {
        switch (face) {
            case UP:
                return up;
            case DOWN:
                return down;
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case EAST:
                return east;
            case WEST:
                return west;
            default:
                return EnumIoMode.DISABLED;
        }
    }

}
