package me.werl.oilcraft;

import me.werl.oilcraft.data.ModData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTabOilCraft extends CreativeTabs {

    public CreativeTabOilCraft() {
        super(ModData.ID);
    }

    @Override
    public Item getTabIconItem() {
        return Items.DIAMOND;
    }

}
