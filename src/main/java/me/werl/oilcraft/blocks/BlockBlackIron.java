package me.werl.oilcraft.blocks;

import me.werl.oilcraft.OilCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockBlackIron extends Block{
    public BlockBlackIron() {
        super(Material.IRON, MapColor.BLACK);
        this.setCreativeTab(OilCraft.creativeTab);

        this.setRegistryName("black_iron");
        this.setUnlocalizedName(getRegistryName().toString());
    }
}
