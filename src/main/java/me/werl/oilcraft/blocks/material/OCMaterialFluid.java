package me.werl.oilcraft.blocks.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;

public class OCMaterialFluid extends MaterialLiquid {
    public OCMaterialFluid(MapColor color) {
        super(color);
    }

    @Override
    public boolean blocksMovement() {
        return true;
    }
}
