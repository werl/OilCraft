package me.werl.oilcraft.fluids;

import me.werl.oilcraft.blocks.material.OCMaterialFluid;
import net.minecraft.block.material.MapColor;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidOil extends BlockFluidOilC {



    public BlockFluidOil(Fluid fluid) {
        super(fluid, new OCMaterialFluid(MapColor.BLACK), 0.4, 0x0);
        this.setDensity(800);
    }

}
