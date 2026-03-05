package team.lodestar.lodestone.modules.datagen.smith.blockstate;

import net.minecraft.world.level.block.Block;
import team.lodestar.lodestone.modules.datagen.ItemModelSmithTypes;
import team.lodestar.lodestone.modules.datagen.smith.itemmodel.ItemModelSmith;
import team.lodestar.lodestone.modules.datagen.providers.block.LodestoneBlockStateSystem;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    protected final SmithStateSupplier<T> stateSupplier;
    protected final ItemModelSmith itemModelSmith;

    public BlockStateSmith(Class<T> blockClass, SmithStateSupplier<T> stateSupplier) {
        this(blockClass, ItemModelSmithTypes.BLOCK_MODEL_ITEM, stateSupplier);
    }

    public BlockStateSmith(Class<T> blockClass, ItemModelSmith itemModelSmith, SmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
        this.itemModelSmith = itemModelSmith;
    }

    @SafeVarargs
    public final void act(BlockStateSystemData data, Supplier<? extends Block>... blocks) {
        act(data, List.of(blocks));
    }

    public void act(BlockStateSystemData data, Collection<Supplier<? extends Block>> blocks) {
        for (Supplier<? extends Block> block : blocks) {
            tryAct(data, itemModelSmith, block, stateSupplier::act);
        }
    }

    public interface SmithStateSupplier<T extends Block> {
        void act(T block, LodestoneBlockStateSystem provider);
    }
}