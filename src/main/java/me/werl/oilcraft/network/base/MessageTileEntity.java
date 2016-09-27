package me.werl.oilcraft.network.base;

import com.google.common.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class MessageTileEntity<T extends TileEntity> implements IMessage {

    protected int x;
    protected int y;
    protected int z;

    protected MessageTileEntity() {
    }

    protected MessageTileEntity(T tile) {
        x = tile.getPos().getX();
        y = tile.getPos().getY();
        z = tile.getPos().getZ();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @SuppressWarnings("unchecked")
    protected T getTileEntity(World world) {
        if(world == null) {
            return null;
        }
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if(tile == null) {
            return null;
        }
        TypeToken<?> tileType = TypeToken.of(getClass()).resolveType(MessageTileEntity.class.getTypeParameters()[0]);
        if(tileType.isAssignableFrom(tile.getClass())) {
            return (T) tile;
        }
        return null;
    }

    protected World getWorld(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity.worldObj;
    }

}
