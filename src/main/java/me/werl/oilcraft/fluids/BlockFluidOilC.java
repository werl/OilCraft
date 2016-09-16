package me.werl.oilcraft.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidOilC extends BlockFluidClassic {

    protected final double speedModifier;
    protected float fogColorRed = 1;
    protected float fogColorGreen = 1;
    protected float fogColorBlue = 1;

    public BlockFluidOilC(Fluid fluid, Material material, double speedModifier, int fogColor) {
        super(fluid, material);
        this.speedModifier = speedModifier;

        float dim = 1;
        while (fogColorRed > .2f || fogColorGreen > .2f || fogColorBlue > .2f) {
            fogColorRed = (fogColor >> 16 & 255) / 255f * dim;
            fogColorGreen = (fogColor >> 8 & 255) / 255f * dim;
            fogColorBlue = (fogColor & 255) / 255f * dim;
            dim *= .9f;
        }
    }

    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos pos, IBlockState state, Entity entity, double yToTest, Material material, boolean testingHead) {
        if(material == Material.WATER) {
            return Boolean.TRUE;
        }
        return null;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        entityIn.motionX *= speedModifier;
        entityIn.motionY *= speedModifier;
        entityIn.motionZ *= speedModifier;
    }

    public float getFogColorRed() {
        return fogColorRed;
    }

    public float getFogColorGreen() {
        return fogColorGreen;
    }

    public float getFogColorBlue() {
        return fogColorBlue;
    }

}
