package me.werl.oilcraft.init;

import me.werl.oilcraft.blocks.BlockBlackIron;
import me.werl.oilcraft.blocks.BlockMachine;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.items.blocks.ItemMachine;
import me.werl.oilcraft.tileentity.TileSBRefinery;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

@GameRegistry.ObjectHolder(ModData.ID)
public class ModBlocks {

    @GameRegistry.ObjectHolder("block_machine")
    public static final BlockMachine BLOCK_MACHINE = new BlockMachine();
    @GameRegistry.ObjectHolder("black_iron")
    public static final BlockBlackIron BLOCK_BLACK_IRON = new BlockBlackIron();

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();

        /**
         * Register this mods {@link Block}s
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            registry.registerAll(
                    BLOCK_MACHINE,
                    BLOCK_BLACK_IRON
            );
        }

        /**
         * Register this mod's {@link ItemBlock}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
            final ItemBlock[] items = {
                    new ItemMachine(BLOCK_MACHINE),
                    new ItemBlock(BLOCK_BLACK_IRON)
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final ItemBlock item : items) {
                registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
                ITEM_BLOCKS.add(item);
            }
        }
    }

    public static void registerTileEntities() {
        registerTileEntity(TileSBRefinery.class);
    }

    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass) {
        GameRegistry.registerTileEntity(tileEntityClass, ModData.ID + tileEntityClass.getSimpleName().replaceFirst("Tile", ""));
    }

}
