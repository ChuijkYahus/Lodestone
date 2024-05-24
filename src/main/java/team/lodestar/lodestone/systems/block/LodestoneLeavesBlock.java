package team.lodestar.lodestone.systems.block;

import net.minecraft.client.color.block.*;
import net.minecraft.util.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.common.extensions.*;
import team.lodestar.lodestone.systems.easing.*;

import java.awt.*;

public class LodestoneLeavesBlock extends LeavesBlock implements IForgeBlock {

    public final IntegerProperty colorProperty;
    public final Color minColor;
    public final Color maxColor;

    public LodestoneLeavesBlock(Properties properties, IntegerProperty colorProperty, Color minColor, Color maxColor) {
        super(properties);
        this.minColor = minColor;
        this.maxColor = maxColor;
        this.colorProperty = colorProperty;
        registerDefaultState(defaultBlockState().setValue(colorProperty, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED, colorProperty);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(colorProperty, 0);
    }

    public static void registerSimpleGradientColors(BlockColors blockColors, LodestoneLeavesBlock leavesBlock) {
        blockColors.register((s, l, p, c) -> {
            float colorMax = leavesBlock.colorProperty.getPossibleValues().size();
            float color = s.getValue(leavesBlock.colorProperty);
            float pct = (colorMax - (color / colorMax));
            float value = Easing.SINE_IN_OUT.ease(pct, 0, 1, 1);
            int red = (int) Mth.lerp(value, leavesBlock.minColor.getRed(), leavesBlock.maxColor.getRed());
            int green = (int) Mth.lerp(value, leavesBlock.minColor.getGreen(), leavesBlock.maxColor.getGreen());
            int blue = (int) Mth.lerp(value, leavesBlock.minColor.getBlue(), leavesBlock.maxColor.getBlue());
            return red << 16 | green << 8 | blue;
        }, leavesBlock);
    }
}