package me.werl.oilcraft.client.model;

import me.werl.oilcraft.data.EnumMachines;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.init.ModBlocks;
import me.werl.oilcraft.init.ModFluids;
import me.werl.oilcraft.init.ModItems;
import me.werl.oilcraft.util.IVariant;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ModModelManager {

    public static final ModModelManager INSTANCE = new ModModelManager();

    private static final String FLUID_MODEL_PATH = ModData.RESOURCE_PREFIX + "fluid";

    private ModModelManager() {}

    /**
     * Register this mod's fluid, block and item models.
     *
     * @param event The event
     */
    @SubscribeEvent
    public static void registerAllModels(ModelRegistryEvent event) {
        INSTANCE.registerFluidModels();
        INSTANCE.registerBlockModels();
        INSTANCE.registerItemModels();
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

        itemsRegistered.add(item);
    }

    private void registerBlockModels() {

        for(final EnumMachines machine : EnumMachines.values()) {
            registerBlockItemModelForMeta(ModBlocks.BLOCK_MACHINE, machine.getMeta(), "block_machine", machine.getName());
        }
        registerBlockItemModel(ModBlocks.BLOCK_BLACK_IRON);
    }

    private void registerBlockItemModel(Block block) {
        final Item item = Item.getItemFromBlock(block);
        if (item != null) {
            registerItemModel(item);
        }
    }

    private void registerBlockItemModel(Block block, String modelLocation) {
        final Item item = Item.getItemFromBlock(block);
        if (item != null) {
            registerItemModel(item, modelLocation);
        }
    }

    private <T extends IVariant> void registerVariantBlockItemModels(Block block, String variantName, T[] variants) {
        final Item item = Item.getItemFromBlock(block);
        if (item != null) {
            registerVariantItemModels(item, variantName, variants);
        }
    }

    private void registerBlockItemModel(Block block, ModelResourceLocation fullModelLocation) {
        final Item item = Item.getItemFromBlock(block);
        if (item != null) {
            registerItemModel(item, fullModelLocation);
        }
    }

    private void registerBlockItemModelForMeta(Block block, int metadata, String blockName, String variantName) {
        final Item item = Item.getItemFromBlock(block);
        final ModelResourceLocation location = new ModelResourceLocation(ModData.RESOURCE_PREFIX + "block/" + blockName + "/" + variantName);
        registerItemModelForMeta(item, metadata, location);
    }

    private final Set<Item> itemsRegistered = new HashSet<>();

    private void registerItemModels() {
        registerItemModel(ModItems.WRENCH);
    }

    private void registerItemModel(Item item) {
        registerItemModel(item, item.getRegistryName().toString());
    }

    private void registerItemModel(Item item, String modelLocation) {
        final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
        registerItemModel(item, fullModelLocation);
    }

    private void registerItemModel(Item item, ModelResourceLocation fullModelLocation) {
        ModelBakery.registerItemVariants(item, fullModelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
        registerItemModel(item, MeshDefinitionFix.create(stack -> fullModelLocation));
    }

    private void registerItemModel(Item item, ItemMeshDefinition meshDefinition) {
        itemsRegistered.add(item);
        ModelLoader.setCustomMeshDefinition(item, meshDefinition);
    }

    private <T extends IVariant> void registerVariantItemModels(Item item, String variantName, T[] variants) {
        for (T variant : variants) {
            registerItemModelForMeta(item, variant.getMeta(), variantName + "=" + variant.getName());
        }
    }

    private void registerItemModelForMeta(Item item, int metadata, String variant) {
        registerItemModelForMeta(item, metadata, new ModelResourceLocation(item.getRegistryName(), variant));
    }

    private void registerItemModelForMeta(Item item, int metadata, ModelResourceLocation modelResourceLocation) {
        itemsRegistered.add(item);
        ModelLoader.setCustomModelResourceLocation(item, metadata, modelResourceLocation);
    }

}
