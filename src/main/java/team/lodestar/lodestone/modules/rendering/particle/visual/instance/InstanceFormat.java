package team.lodestar.lodestone.modules.rendering.particle.visual.instance;

import java.util.ArrayList;
import java.util.List;

public record InstanceFormat(List<InstanceElement> elements, int totalFloats, int strideBytes) {

    public static class Builder {
        private final List<InstanceElement> elements = new ArrayList<>();
        private int totalFloats = 0;

        public Builder add(int floatCount, InstanceWriter writer) {
            elements.add(new InstanceElement(floatCount, writer));
            totalFloats += floatCount;
            return this;
        }

        public InstanceFormat build() {
            return new InstanceFormat(List.copyOf(elements), totalFloats, totalFloats * Float.BYTES);
        }
    }
}