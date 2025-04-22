package team.lodestar.lodestone.systems.network.particle;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NetworkedParticleEffectExtraData {

    public static final String ITEM = "stack";

    public final CompoundTag compoundTag;

    public NetworkedParticleEffectExtraData(CompoundTag compoundTag) {
        this.compoundTag = compoundTag;
    }

    public NetworkedParticleEffectExtraData() {
        this(new CompoundTag());
    }

    public NetworkedParticleEffectExtraData(HolderLookup.Provider registryAccess, ItemStack stack) {
        this();
        compoundTag.put(ITEM, stack.save(registryAccess));
    }

    public ItemStack getStack(HolderLookup.Provider registryAccess) {
        return compoundTag.contains(ITEM) ? ItemStack.parseOptional(registryAccess, compoundTag.getCompound(ITEM)) : ItemStack.EMPTY;
    }
}
