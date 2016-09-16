package me.werl.oilcraft;

import me.werl.oilcraft.data.ModData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by PeterLewis on 2016-08-29.
 */
public class CreativeTabOilCraft extends CreativeTabs {

    public CreativeTabOilCraft() {
        super(ModData.ID);
    }

    @Override
    public Item getTabIconItem() {
        return Items.DIAMOND;
    }

}
