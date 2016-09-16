package me.werl.oilcraft.network;

import me.werl.oilcraft.client.gui.GuiHeatGenerator;
import me.werl.oilcraft.inventories.ContainerHeatGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import static me.werl.oilcraft.data.GuiData.*;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IInventory tile = (IInventory) world.getTileEntity(new BlockPos(x, y, z));

        switch (ID) {
            case HEAT_GENERATOR_ID:
                return new ContainerHeatGenerator(player.inventory, tile);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IInventory tile = (IInventory) world.getTileEntity(new BlockPos(x, y, z));

        switch (ID) {
            case HEAT_GENERATOR_ID:
                return new GuiHeatGenerator(player.inventory, tile);
            default:
                return null;
        }
    }
}
