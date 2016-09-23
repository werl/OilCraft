package me.werl.oilcraft.blocks;

import me.werl.oilcraft.OilCraft;
import me.werl.oilcraft.data.ModData;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockOilCraft extends Block {

    public BlockOilCraft(Material material, MapColor mapColor, String blockName) {
        super(material, mapColor);
        setBlockName(this, blockName);
        setCreativeTab(OilCraft.creativeTab);
    }

    public BlockOilCraft(Material material, String blockName) {
        this(material, material.getMaterialMapColor(), blockName);
    }

    /**
     * Set the registry name of {@code block} to {@code blockName} and unlocalized name to the full registry name;
     *
     * @param block The block
     * @param blockName The block's name
     */
    public static void setBlockName(Block block, String blockName) {
        block.setRegistryName(ModData.ID, blockName);
        block.setUnlocalizedName(block.getRegistryName().toString());
    }

}
