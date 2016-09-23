package me.werl.oilcraft.fluids.tanks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Borrowed From Buildcraft
 */
public class FilteredTank extends FluidTank implements INBTSerializable<NBTTagCompound> {

    @Nonnull
    private final String name;

    @Nonnull
    private final Predicate<FluidStack> filter;

    /**
     * Creates a tank with the given name and capacity (in milibuckets) with no filter set
     *
     * @param name FilteredTank name
     * @param capacity Amount of fluid held (in milibuckets)
     * @param tile The TileEntity that is using this tank
     */
    public FilteredTank(@Nonnull String name, int capacity, TileEntity tile) {
        this(name, capacity, tile, null);
    }

    public FilteredTank(@Nonnull String name, int capacity, TileEntity tile, @Nullable Predicate<FluidStack> filter) {
        super(capacity);
        this.name = name;
        this.tile = tile;
        this.filter = filter == null ? ((f) -> true) : filter;
    }

    @Nonnull
    public String getTankName() {
        return this.name;
    }

    public boolean isEmpty() {
        FluidStack stack = this.getFluid();
        return stack == null || stack.amount <= 0;
    }

    public boolean isFull() {
        FluidStack stack = this.getFluid();
        return stack != null && stack.amount >= this.getCapacity();
    }

    public Fluid getFluidType() {
        FluidStack stack = this.getFluid();
        return stack != null ? stack.getFluid() : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        readFromNBT(nbt);
    }

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tankData = new NBTTagCompound();
        super.writeToNBT(tankData);
        this.writeTankToNBT(tankData);
        nbt.setTag(this.name, tankData);

        return nbt;
    }

    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {
        if(nbt.hasKey(this.name)) {
            // allow to read empty tanks
            this.setFluid(null);

            NBTTagCompound tankData = nbt.getCompoundTag(this.name);
            super.readFromNBT(tankData);
            this.readTankFromNBT(tankData);
        }
        return this;
    }

    /** Writes some additional information to the nbt */
    public void writeTankToNBT(NBTTagCompound nbt) {}

    /** Reads some additional information to the nbt */
    public void readTankFromNBT(NBTTagCompound nbt) {}

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if(this.filter.test(resource))
            return super.fill(resource, doFill);
        return 0;
    }

    @Override
    public void setFluid(@Nullable FluidStack fluid) {
        if(fluid == null || this.filter.test(fluid))
            super.setFluid(fluid);
    }
}
