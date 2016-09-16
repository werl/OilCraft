package me.werl.oilcraft.client.model;

import me.werl.oilcraft.data.EnumMachines;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.init.ModBlocks;
import me.werl.oilcraft.init.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModModelManager {

    public static final ModModelManager INSTANCE = new ModModelManager();

    private static final String FLUID_MODEL_PATH = ModData.RESOURCE_PREFIX + "fluid";

    private ModModelManager() {}

    public void registerAllModels() {
        this.registerFluidModels();
        this.registerItemBlockModels();
    }

    private void registerFluidModels() {
        ModFluids.MOD_FLUID_BLOCKS.forEach(this::registerFluidModel);
    }

    private void registerFluidModel(IFluidBlock fluidBlock) {
        final Item item = Item.getItemFromBlock((Block) fluidBlock);
        assert item != null;
        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FLUID_MODEL_PATH, fluidBlock.getFluid().getName());

        ModelBakery.registerItemVariants(item);


        ModelLoader.setCustomMeshDefinition(item, stack -> modelResourceLocation);

        ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return modelResourceLocation;
            }
        });

        //itemsRegistered.add(item);
    }

    private void registerItemBlockModels() {
        this.registerItemBlockModel(ModBlocks.BLOCK_MACHINE, EnumMachines.class);
    }

    private < E extends Enum<E> & IStringSerializable > void registerItemBlockModel(Block block, Class<E> variants) {
        final Item item = Item.getItemFromBlock(block);
        assert item != null;

        ModelLoader.setCustomStateMapper(block, new StateMap.Builder().build());

        for (E type: variants.getEnumConstants()) {
            ModelResourceLocation location = new ModelResourceLocation(ModData.RESOURCE_PREFIX +
                    type.getName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), location);
        }

    }

}
