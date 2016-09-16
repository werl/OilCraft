package me.werl.oilcraft.world.gen;

import me.werl.oilcraft.config.Config;
import me.werl.oilcraft.data.FluidData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenOil implements IWorldGenerator{

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(!(chunkGenerator instanceof ChunkProviderFlat)) {
            switch (world.provider.getDimensionType()) {
                case OVERWORLD:
                    generateSurface(world, random, chunkX * 16, chunkZ * 16);
                    break;
                case NETHER:
                    break;
                case THE_END:
                    break;
                default:
                    generateSurface(world, random, chunkX * 16, chunkZ * 16);
            }
        }
    }

    public void generateSurface(World world, Random rand, int blockX, int blockZ) {
        if(rand.nextDouble() < Config.oilGenLakeChance / 100) {
            int y = rand.nextInt(rand.nextInt(128) + 8);

            new WorldGenLakes(FluidRegistry.getFluid(FluidData.FLUID_OIL).getBlock())
                    .generate(world, rand, new BlockPos(blockX + 8, y, blockZ + 8));
        }
    }

}
