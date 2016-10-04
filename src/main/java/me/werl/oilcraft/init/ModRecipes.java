package me.werl.oilcraft.init;

import me.werl.oilcraft.custom_recipes.RefineryRecipe;
import net.minecraftforge.fluids.FluidStack;

public class ModRecipes {

    public static void registerRecipes() {
        registerRefineryRecipes();
    }

    private static void registerRefineryRecipes() {
        RefineryRecipe holder = new RefineryRecipe(400, 1000, new FluidStack(ModFluids.OIL, 2), new FluidStack(ModFluids.FUEL, 2));
        RefineryRecipe.registerRecipe(holder) ;
    }

}
