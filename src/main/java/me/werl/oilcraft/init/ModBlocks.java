package me.werl.oilcraft.init;

import me.werl.oilcraft.blocks.BlockBlackIron;
import me.werl.oilcraft.blocks.BlockMachine;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.items.blocks.ItemMachine;
import me.werl.oilcraft.tileentity.TileHeatGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ModBlocks {

    public static final Set<Block> BLOCKS = new HashSet<>();

    public static final BlockMachine BLOCK_MACHINE;
    public static final BlockBlackIron BLOCK_BLACK_IRON;

    static {
        BLOCK_MACHINE = registerBlock(new BlockMachine(), ItemMachine::new);
        BLOCK_BLACK_IRON = registerBlock(new BlockBlackIron());
    }


    public static void registerBlocks() {
        // Dummy method to make sure static initializer runs
    }

    /**
     * Register a Block with the default ItemBlock class.
     *
     * @param block   The Block instance
     * @param <BLOCK> The Block type
     * @return The Block instance
     */
    protected static <BLOCK extends Block> BLOCK registerBlock(BLOCK block) {
        return registerBlock(block, ItemBlock::new);
    }

    /**
     * Register a Block with a custom ItemBlock class.
     *
     * @param <BLOCK>     The Block type
     * @param block       The Block instance
     * @param itemFactory A function that creates the ItemBlock instance, or null if no ItemBlock should be created
     * @return The Block instance
     */
    protected static <BLOCK extends Block> BLOCK registerBlock(BLOCK block, @Nullable Function<BLOCK, ItemBlock> itemFactory) {
        GameRegistry.register(block);

        if (itemFactory != null) {
            final ItemBlock itemBlock = itemFactory.apply(block);

            GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
        }

        BLOCKS.add(block);
        return block;
    }

    public static void registerTileEntities() {
        registerTileEntity(TileHeatGenerator.class);
    }

    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass) {
        GameRegistry.registerTileEntity(tileEntityClass, ModData.ID + tileEntityClass.getSimpleName().replaceFirst("Tile", ""));
    }

}
