package me.werl.oilcraft.network;

import io.netty.buffer.ByteBuf;
import me.werl.oilcraft.OilCraft;
import me.werl.oilcraft.network.base.MessageTileEntity;
import me.werl.oilcraft.tileentity.TileSBRefinery;
import me.werl.oilcraft.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSBRTank extends MessageTileEntity<TileSBRefinery> implements IMessageHandler<PacketSBRTank, IMessage> {

    private NBTTagCompound nbtRoot;

    public PacketSBRTank() {
    }

    public PacketSBRTank(TileSBRefinery tile) {
        super(tile);
        nbtRoot = new NBTTagCompound();
        if(tile.inputTank.getFluidAmount() > 0) {
            NBTTagCompound inputTank = new NBTTagCompound();
            tile.inputTank.writeToNBT(inputTank);
            nbtRoot.setTag("input", inputTank);
        }
        if(tile.outputTank.getFluidAmount() > 0) {
            NBTTagCompound outputTank = new NBTTagCompound();
            tile.outputTank.writeToNBT(outputTank);
            nbtRoot.setTag("output", outputTank);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        NetworkUtil.writeNBTTagCompound(nbtRoot, buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        nbtRoot = NetworkUtil.readNBTTagCompound(buf);
    }


    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(PacketSBRTank message, MessageContext ctx) {
        EntityPlayer player = OilCraft.proxy.getClientPlayer();
        TileSBRefinery tile = message.getTileEntity(player.worldObj);
        if(tile == null)
            return null;
        if(message.nbtRoot.hasKey("input")) {
            NBTTagCompound inputTank = message.nbtRoot.getCompoundTag("input");
            tile.inputTank.readFromNBT(inputTank);
        } else {
            tile.inputTank.setFluid(null);
        }
        if(message.nbtRoot.hasKey("output")) {
            NBTTagCompound outputTank = message.nbtRoot.getCompoundTag("output");
            tile.outputTank.readFromNBT(outputTank);
        } else {
            tile.outputTank.setFluid(null);
        }
        return null;
    }
}
