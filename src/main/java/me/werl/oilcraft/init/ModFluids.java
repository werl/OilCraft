package me.werl.oilcraft.init;

import me.werl.oilcraft.OilCraft;
import me.werl.oilcraft.blocks.material.OCMaterialFluid;
import me.werl.oilcraft.data.FluidData;
import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.fluids.BlockFluidCoolant;
import me.werl.oilcraft.fluids.BlockFluidOil;
import me.werl.oilcraft.fluids.BlockFluidOilC;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModFluids {

    public static final Fluid OIL;
    public static final Fluid COOLANT;

    /**
     * The fluids registered by this mod. Includes fluids that were already registered by another mod.
     */
    public static final Set<Fluid> FLUIDS = new HashSet<>();

    /**
     * The fluid blocks from this mod only. Doesn't include blocks for fluids that were already registered by another mod.
     */
    public static final Set<IFluidBlock> MOD_FLUID_BLOCKS = new HashSet<>();

    static {
        OIL = createFluid(FluidData.FLUID_OIL, true,
                fluid -> fluid.setLuminosity(0).setDensity(800).setViscosity(10000), BlockFluidOil::new);
        COOLANT = createFluid(FluidData.FLUID_COOLANT, true,
                fluid -> fluid.setLuminosity(0).setDensity(250).setViscosity(250), BlockFluidCoolant::new);
    }

    public static void registerFluids() {
        // Dummy method to make sure the static initialiser runs
    }

    public static void registerFluidContainers() {
        for (final Fluid fluid : FLUIDS) {
            registerBucket(fluid);
            registerTank(fluid);
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
            registerFluidBlock(blockFactory.apply(fluid));
        } else {
            fluid = FluidRegistry.getFluid(name);
        }

        FLUIDS.add(fluid);

        return fluid;
    }

    private static <T extends Block & IFluidBlock> T registerFluidBlock(T block) {
        block.setRegistryName("fluid." + block.getFluid().getName());
        block.setUnlocalizedName(ModData.RESOURCE_PREFIX + block.getFluid().getUnlocalizedName());
        block.setCreativeTab(OilCraft.creativeTab);

        ModBlocks.registerBlock(block);

        MOD_FLUID_BLOCKS.add(block);

        return block;
    }

    private static void registerBucket(Fluid fluid) {
        FluidRegistry.addBucketForFluid(fluid);
    }

    private static void registerTank(Fluid fluid) {
        // final FluidStack fluidStack = new FluidStack(fluid, TileEntityFluidTank.CAPACITY);
        // ((ItemFluidTank) Item.getItemFromBlock(ModBlocks.FLUID_TANK)).addFluid(fluidStack);
    }

}
