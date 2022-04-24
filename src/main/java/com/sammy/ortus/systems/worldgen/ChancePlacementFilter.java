package com.sammy.ortus.systems.worldgen;

import com.mojang.serialization.Codec;
import com.sammy.ortus.setup.OrtusPlacementFillers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.Random;

public class ChancePlacementFilter extends PlacementFilter {
   public static final Codec<ChancePlacementFilter> CODEC = ExtraCodecs.POSITIVE_FLOAT.fieldOf("chance").xmap(ChancePlacementFilter::new, (p_191907_) -> p_191907_.chance).codec();
   private final float chance;

   public ChancePlacementFilter(float chance) {
      this.chance = chance;
   }

   @Override
   protected boolean shouldPlace(PlacementContext p_191903_, Random p_191904_, BlockPos p_191905_) {
      return p_191904_.nextFloat() < chance;
   }

   public PlacementModifierType<?> type() {
      return OrtusPlacementFillers.CHANCE;
   }
}