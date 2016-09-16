package me.werl.oilcraft.tileentity;

import me.werl.oilcraft.tesla.TeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by PeterLewis on 2016-08-25.
 */
public abstract class TileTESLA extends TileInventory {

    private TeslaContainer teslaContainer;

    public TileTESLA(int slots) {
        super(slots);
        this.teslaContainer = new TeslaContainer();
    }

    public TileTESLA(int slots, TeslaContainer container) {
        super(slots);
        teslaContainer = container;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.teslaContainer = new TeslaContainer(tag.getCompoundTag("TeslaContainer"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setTag("TeslaContainer", this.teslaContainer.serializeNBT());

        return super.writeToNBT(tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
        if((capability == TeslaCapabilities.CAPABILITY_CONSUMER && this.teslaContainer.isConsumer()) ||
                (capability == TeslaCapabilities.CAPABILITY_PRODUCER && this.teslaContainer.isProducer()) ||
                (capability == TeslaCapabilities.CAPABILITY_HOLDER && this.teslaContainer.isHolder()))
            return (T) this.teslaContainer;

        return super.getCapability(capability,facing);
    }

    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
        if((capability == TeslaCapabilities.CAPABILITY_CONSUMER && this.teslaContainer.isConsumer()) ||
                (capability == TeslaCapabilities.CAPABILITY_PRODUCER && this.teslaContainer.isProducer()) ||
                (capability == TeslaCapabilities.CAPABILITY_HOLDER && this.teslaContainer.isHolder()))
            return true;

        return super.hasCapability(capability, facing);
    }
}
