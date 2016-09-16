package me.werl.oilcraft.tesla;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by PeterLewis on 2016-08-25.
 */
public class TeslaContainer implements ITeslaConsumer, ITeslaProducer, ITeslaHolder, INBTSerializable<NBTTagCompound> {

    // amount of TESLA stored
    private long stored;
    // max TESLA
    private long capacity;
    // max TESLA in
    private long inputRate;
    // max TESLA out
    private long outputRate;

    /**
     * Default constructor.
     *
     * capacity = 5000
     * stored = 0
     * Tesla I/O = 50
     */
    public TeslaContainer() {
        this (5000, 50);
    }

    /**
     * Constructor for setting capacity and I/O rate
     *
     * @param capacity Max stored Tesla
     * @param ioRate Both the input and output rate
     */
    public TeslaContainer(long capacity, long ioRate) {
        this(capacity, ioRate, ioRate);
    }

    /**
     * Constructor for custom capacity, input rate, and output rate
     *
     * @param capacity Max stored Tesla
     * @param inputRate Max Tesla input/tick
     * @param outputRate Max tesla output/tick
     */
    public TeslaContainer(long capacity, long inputRate, long outputRate) {
        this(0, capacity, inputRate, outputRate);
    }

    /**
     * Constructor for all custom values
     *
     * @param power Stored Tesla
     * @param capacity Max stored Tesla
     * @param inputRate Max Tesla input/tick
     * @param outputRate Max Tesla output/tick
     */
    public TeslaContainer(long power, long capacity, long inputRate, long outputRate) {
        this.stored = power;
        this.capacity = capacity;
        this.inputRate = inputRate;
        this.outputRate = outputRate;
    }

    /**
     * Constructor that takes an NBTTagCompound to get values
     *
     * @param tag NBTTagCompound with saved Tesla data
     */
    public TeslaContainer(NBTTagCompound tag) {
        this.deserializeNBT(tag);
    }

    /**
     * Used to give power to the TeslaContainer
     *
     * @param power Amount of Tesla to add
     * @param simulated If the Tesla should be added
     * @return Tesla that is accepted
     */
    @Override
    public long givePower(long power, boolean simulated) {
        final long acceptedPower = Math.min(this.getCapacity() - this.getStoredPower(), Math.min(this.getInputRate(), power));

        if(!simulated)
            this.stored += acceptedPower;

        return acceptedPower;
    }

    /**
     * Used to take power from the TeslaContainer
     *
     * @param power Amount of Tesla to remove
     * @param simulated If the Tesla should be removed
     * @return Tesla that is taken
     */
    @Override
    public long takePower(long power, boolean simulated) {
        final long removedPower = Math.min(this.stored, Math.min(this.outputRate, power));

        if(!simulated)
            this.stored -= removedPower;

        return removedPower;
    }

    /**
     * Cets stored Tesla
     *
     * @return Stored Tesla
     */
    @Override
    public long getStoredPower() {
        return this.stored;
    }

    /**
     * Gets capacity (max Tesla)
     *
     * @return Max stored Tesla
     */
    @Override
    public long getCapacity() {
        return this.capacity;
    }

    /**
     * Used to store the Tesla data to a NBTTagCompound
     *
     * @return An NBTTagCompound that has the Tesla data in it
     */
    @Override
    public NBTTagCompound serializeNBT() {
        final NBTTagCompound dataTag = new NBTTagCompound();
        dataTag.setLong("TeslaPower", this.stored);
        dataTag.setLong("TeslaCapacity", this.capacity);
        dataTag.setLong("TeslaInput", this.inputRate);
        dataTag.setLong("TeslaOutput", this.outputRate);

        return dataTag;
    }

    /**
     * Used to get the stored Tesla data from the given NBTTagCompound
     *
     * @param tag The tag that has the stored Tesla Data
     */
    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.stored = tag.getLong("TeslaPower");

        if(tag.hasKey("TeslaCapacity"))
            this.capacity = tag.getLong("TeslaCapacity");

        if(tag.hasKey("TeslaInput"))
            this.inputRate = tag.getLong("TeslaInput");

        if(tag.hasKey("TeslaOutput"))
            this.outputRate = tag.getLong("TeslaOutput");

        if(this.stored > this.getCapacity())
            this.stored = this.getCapacity();
    }

    /**
     * Used to set capacity
     *
     * @param capacity The new capacity
     * @return Self
     */
    public TeslaContainer setCapacity(long capacity) {
        this.capacity = capacity;

        if(this.stored > this.capacity)
            this.stored = capacity;
        return this;
    }

    /**
     * Gets the Tesla input rate
     *
     * @return Tesla input rate
     */
    public long getInputRate() {
        return this.inputRate;
    }

    /**
     * Used to set Tesla input rate
     *
     * @param rate New rate;
     * @return Self
     */
    public TeslaContainer setInputRate(long rate) {
        this.inputRate = rate;
        return this;
    }

    /**
     * Gets the Tesla output rate
     *
     * @return Tesla output rate
     */
    public long getOutputRate() {
        return this.outputRate;
    }

    /**
     * Used to set Tesla output rate
     *
     * @param rate New rate
     * @return Self
     */
    public TeslaContainer setOutputRate(long rate) {
        this.outputRate = rate;
        return this;
    }

    /**
     * Used to set the input and output to the same value
     *
     * @param rate The new I/O rate
     * @return Self
     */
    public TeslaContainer setTransferRate(long rate) {
        this.inputRate = rate;
        this.outputRate = rate;
        return this;
    }

    /**
     * Determine if this has the CAPABILITY_CONSUMER capability
     *
     * @return true if this has CAPABILITY_CONSUMER
     */
    public boolean isConsumer() {
        return this.inputRate > 0;
    }

    /**
     * Determine if this has the CAPABILITY_PRODUCER capability
     *
     * @return true if this has CAPABILITY_PRODUCER
     */
    public boolean isProducer() {
        return this.outputRate > 0;
    }

    /**
     * Determine if this has the CAPABILITY_HOLDER capability
     *
     * @return True if this has CAPABILITY_HOLDER
     */
    public boolean isHolder() {
        return this.capacity > 0;
    }

}
