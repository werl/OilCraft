package me.werl.oilcraft.fluids;

import me.werl.oilcraft.blocks.material.OCMaterialFluid;
import net.minecraft.block.material.MapColor;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidCoolant extends BlockFluidOilC {

    public BlockFluidCoolant(Fluid fluid) {
        super(fluid, new OCMaterialFluid(MapColor.CYAN), 0.999, 0x3B92A5);
        this.setDensity(250);
    }

}
