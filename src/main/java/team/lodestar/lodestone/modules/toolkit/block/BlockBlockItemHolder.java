package team.lodestar.lodestone.modules.toolkit.block;


import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public record BlockBlockItemHolder<T extends Block, K extends BlockItem>(DeferredHolder<Block, T> block, DeferredHolder<Item, K> item) implements Supplier<T>, ItemLike {

    @Override
    public T get() {
        return block.get();
    }

    public BlockState getDefaultState() {
        return get().defaultBlockState();
    }

    public ItemStack getDefaultInstance() {
        return getItem().getDefaultInstance();
    }

    public K getItem() {
        return item.get();
    }

    public DeferredHolder<Block, T> getBlockHolder() {
        return block;
    }

    public DeferredHolder<Item, K> getItemHolder() {
        return item;
    }

    @Override
    public Item asItem() {
        return getItem();
    }
}