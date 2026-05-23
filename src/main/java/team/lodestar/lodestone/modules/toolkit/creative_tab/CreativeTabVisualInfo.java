package team.lodestar.lodestone.modules.toolkit.creative_tab;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.modules.toolkit.creative_tab.slot.SlotStorage;

import java.util.Optional;

public abstract class CreativeTabVisualInfo {

    public abstract Optional<ResourceLocation> getHeaderTexture(CreativeTabHeader header, int row, int column);

    public abstract Optional<ResourceLocation> getEmptySlotTexture(SlotStorage slot);

}
