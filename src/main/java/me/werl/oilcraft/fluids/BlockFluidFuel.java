package me.werl.oilcraft.fluids;

import me.werl.oilcraft.blocks.material.OCMaterialFluid;
import net.minecraft.block.material.MapColor;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by PeterLewis on 2016-09-25.
 */
public class BlockFluidFuel extends BlockFluidOilC {
    public BlockFluidFuel(Fluid fluid) {
        super(fluid, new OCMaterialFluid(MapColor.YELLOW), 0.9, 0xFEFFBC);
    }
}
