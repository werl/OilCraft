package me.werl.oilcraft.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenOres implements IWorldGenerator {

    //private final WorldGenMinable dirtTar;

    public WorldGenOres() {
        //dirtTar = new WorldGenMinable();
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    }

}
