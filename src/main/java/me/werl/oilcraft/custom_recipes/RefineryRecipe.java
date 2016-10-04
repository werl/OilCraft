package me.werl.oilcraft.custom_recipes;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RefineryRecipe {

    public static final List<RefineryRecipe> RECIPES = new ArrayList<>();

    private final int minTemp;
    private final int maxTemp;
    private final FluidStack input;
    private final FluidStack[] outputs;

    public static void registerRecipe(RefineryRecipe recipe) {
        RECIPES.add(recipe);
    }

    /**
     * A single output recipe for the refinery
     *
     * @param minTemp The minimum temperature at which the recipe can be done
     * @param maxTemp Used to tell if the refinery is too hot. Future Use!
     * @param input The FluidStack that is consumed every cycle
     * @param output The FluidStack that is created every cycle
     */
    public RefineryRecipe(@Nonnegative int minTemp, @Nonnegative int maxTemp, @Nonnull FluidStack input, @Nonnull FluidStack output) {
        this.minTemp = minTemp;

        this.maxTemp = maxTemp;
        this.input = input;
        this.outputs = new FluidStack[] { output };
    }

    /**
     * For future use when there is multiple output tanks per refinery
     *
     * @param minTemp The minimum temperature at which the recipe can be done
     * @param maxTemp Used to tell if the refinery is too hot. Future Use!
     * @param input The FluidStack that is consumed every cycle
     * @param outputs The FluidStacks that is created every cycle
     */
    private RefineryRecipe(@Nonnegative int minTemp, @Nonnegative int maxTemp, @Nonnull FluidStack input, @Nonnull FluidStack... outputs) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.input = input;
        this.outputs = outputs;
    }

    public int getMinTemp() {
        return this.minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public boolean isInTempRange(double temp) {
        return temp >= minTemp && temp <= maxTemp;
    }

    public FluidStack getInput() {
        return input;
    }

    public Fluid getInputFluid() {
        return input.getFluid();
    }

    public int getInputAmount() {
        return input.amount;
    }

    public FluidStack[] getOutputs() {
        return outputs;
    }

}
