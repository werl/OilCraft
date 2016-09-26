package me.werl.oilcraft.init;

import me.werl.oilcraft.OilCraft;
import me.werl.oilcraft.blocks.material.OCMaterialFluid;
import me.werl.oilcraft.data.FluidData;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.fluids.BlockFluidCoolant;
import me.werl.oilcraft.fluids.BlockFluidFuel;
import me.werl.oilcraft.fluids.BlockFluidOil;
import me.werl.oilcraft.fluids.BlockFluidOilC;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModFluids {

    /**
     * The fluids registered by this mod. Includes fluids that were already registered by another mod.
     */
    public static final Set<Fluid> FLUIDS = new HashSet<>();

    /**
     * The fluid blocks from this mod only. Doesn't include blocks for fluids that were already registered by another mod.
     */
    public static final Set<IFluidBlock> MOD_FLUID_BLOCKS = new HashSet<>();

    public static final Fluid OIL = createFluid(FluidData.FLUID_OIL, true,
            fluid -> fluid.setLuminosity(0).setDensity(800).setViscosity(10000), BlockFluidOil::new);
    public static final Fluid COOLANT = createFluid(FluidData.FLUID_COOLANT, true,
            fluid -> fluid.setLuminosity(0).setDensity(250).setViscosity(250), BlockFluidCoolant::new);
    public static final Fluid FUEL = createFluid(FluidData.FLUID_FUEL, true,
            fluid -> fluid.setLuminosity(0).setDensity(400).setViscosity(800), BlockFluidFuel::new);


    public static void registerFluids() {
        // Dummy method to make sure the static initialiser runs
    }

    public static void registerFluidContainers() {
        for (final Fluid fluid : FLUIDS) {
            registerBucket(fluid);
        }
    }

    /**
     * Create a {@link Fluid} and its {@link IFluidBlock}, or use the existing ones if a fluid has already been registered with the same name.
     *
     * @param name                 The name of the fluid
     * @param hasFlowIcon          Does the fluid have a flow icon?
     * @param fluidPropertyApplier A function that sets the properties of the {@link Fluid}
     * @param blockFactory         A function that creates the {@link IFluidBlock}
     * @return The fluid and block
     */
    private static <T extends Block & IFluidBlock> Fluid createFluid(String name, boolean hasFlowIcon, Consumer<Fluid> fluidPropertyApplier, Function<Fluid, T> blockFactory) {
        final String texturePrefix = ModData.RESOURCE_PREFIX + "blocks/";

        final ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
        final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

        Fluid fluid = new Fluid(name, still, flowing);
        final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

        if (useOwnFluid) {
            fluidPropertyApplier.accept(fluid);
            MOD_FLUID_BLOCKS.add(blockFactory.apply(fluid));
        } else {
            fluid = FluidRegistry.getFluid(name);
        }

        FLUIDS.add(fluid);

        return fluid;
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        /**
         * Register this mod's fluid {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCKS) {
                final Block block = (Block) fluidBlock;
                block.setRegistryName(ModData.ID, "fluid." + fluidBlock.getFluid().getName());
                block.setUnlocalizedName(ModData.RESOURCE_PREFIX + fluidBlock.getFluid().getUnlocalizedName());
                block.setCreativeTab(OilCraft.creativeTab);
                registry.register(block);
            }
        }

        /**
         * Register this mod's fluid {@link ItemBlock}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCKS) {
                final Block block = (Block) fluidBlock;
                final ItemBlock itemBlock = new ItemBlock(block);
                itemBlock.setRegistryName(block.getRegistryName());
                registry.register(itemBlock);
            }
        }
    }

    private static void registerBucket(Fluid fluid) {
        FluidRegistry.addBucketForFluid(fluid);
    }

}
