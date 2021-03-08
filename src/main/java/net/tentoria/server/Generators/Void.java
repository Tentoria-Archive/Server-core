package net.tentoria.server.Generators;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class Void extends ChunkGenerator{

    public Void() {
    }

    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biome) {
        ChunkGenerator.ChunkData chunk = this.createChunkData(world);

        for(int xpos = 0; xpos < 16; ++xpos) {
            for(int zpos = 0; zpos < 16; ++zpos) {
                biome.setBiome(xpos, zpos, Biome.PLAINS);
            }
        }

        return chunk;
    }
}
