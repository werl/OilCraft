package me.werl.oilcraft.items.blocks;

import me.werl.oilcraft.data.EnumMachines;
import me.werl.oilcraft.data.ModData;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMachine extends ItemBlock {

    public ItemMachine(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);

        this.setFull3D();
    }

    @Override
    public String getUnlocalizedName (ItemStack stack) {
        return ModData.BLOCK_PREFIX + EnumMachines.values()[stack.getItemDamage()].getName();
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems (Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        for(int i = 0; i < EnumMachines.values().length; i++) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }

}
