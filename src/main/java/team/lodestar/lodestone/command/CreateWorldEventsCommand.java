package team.lodestar.lodestone.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.command.arguments.WorldEventTypeArgument;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypes;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class CreateWorldEventsCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        var create = Commands.literal("create").requires(cs -> cs.hasPermission(2));

        for (WorldEventType type : LodestoneWorldEventTypes.getEventTypes()) {
            var typeArg = Commands.argument("type", WorldEventTypeArgument.worldEventType());

            if (type.commandCodec != null) {
                type.commandCodec.appendTo(typeArg, (source, instance) -> {
                    Level level = source.getLevel();
                    WorldEventHandler.addWorldEvent(level, (WorldEventInstance) instance);
                    return 1;
                });
            }

            create.then(typeArg);
        }

        return create;
    }
}
