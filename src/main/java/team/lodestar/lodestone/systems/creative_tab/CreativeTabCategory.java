package team.lodestar.lodestone.systems.creative_tab;

import com.mojang.datafixers.util.*;
import net.minecraft.world.item.*;

import java.util.*;

public record CreativeTabCategory(String mod, String id,
                                  List<Either<ItemStack, Operation>> items) {
    public String getHeaderLangKey() {
        return mod + ".itemGroup.header." + id;
    }

    public enum Operation {
        NEXT_LINE
    }

    public record CategoryHeader(CreativeTabCategory category) {

    }
}
