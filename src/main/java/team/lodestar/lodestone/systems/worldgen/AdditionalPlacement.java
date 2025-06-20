package team.lodestar.lodestone.systems.worldgen;

import net.minecraft.world.level.*;

@FunctionalInterface
public interface AdditionalPlacement {
    void place(WorldGenLevel level, LodestoneWorldgenBuilderEntry entry);
}