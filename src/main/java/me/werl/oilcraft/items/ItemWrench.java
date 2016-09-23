package me.werl.oilcraft.items;

import me.werl.oilcraft.OilCraft;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.tileentity.interfaces.IDismantlable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends Item {

    public ItemWrench() {
        this.setCreativeTab(OilCraft.creativeTab);
        this.setMaxStackSize(1);
        this.setRegistryName(ModData.ID, "item_wrench");
        this.setUnlocalizedName(getRegistryName().toString());
    }


    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
                                           EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if(!player.isSneaking()) {
            world.getBlockState(pos).getBlock().rotateBlock(world, pos, side);
            return EnumActionResult.SUCCESS;
        } else if(!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);

            if(tile instanceof IDismantlable) {
                world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), ((IDismantlable)tile).dismantale()));
                return EnumActionResult.SUCCESS;
            }
        }


        return EnumActionResult.FAIL;
    }

}
