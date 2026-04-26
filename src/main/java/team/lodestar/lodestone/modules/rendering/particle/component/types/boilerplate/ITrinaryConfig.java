package team.lodestar.lodestone.modules.rendering.particle.component.types.boilerplate;

import team.lodestar.lodestone.modules.core.easing.*;
import team.lodestar.lodestone.modules.rendering.particle.component.types.*;

public interface ITrinaryConfig {

    ConstantLerpOrDoubleLerp getMode();

    float[] getValues();

    Easing[] getEasings();
}
