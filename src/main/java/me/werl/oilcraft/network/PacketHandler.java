package me.werl.oilcraft.network;

import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.network.base.ThreadedNetworkWrapper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketHandler {

    public static final ThreadedNetworkWrapper INSTANCE = new ThreadedNetworkWrapper(ModData.ID);

    private static int ID = 0;

    public static int nextID() {
        return ID ++;
    }

    public static void sendToAllAround(IMessage message, TileEntity te, int range) {
        BlockPos p = te.getPos();
        INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), p.getX(), p.getY(), p.getZ(), range));
    }

    public static void sendToAllAround(IMessage message, TileEntity te) {
        sendToAllAround(message, te, 64);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

}
