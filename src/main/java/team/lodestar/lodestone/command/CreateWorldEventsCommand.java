package team.lodestar.lodestone.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.command.arguments.WorldEventTypeArgument;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypes;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.Arrays;

public class CreateWorldEventsCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        var create = Commands.literal("create").requires(cs -> cs.hasPermission(2));

        for (WorldEventType type : LodestoneWorldEventTypes.getEventTypes()) {
            var typeArg = Commands.argument("type", WorldEventTypeArgument.worldEventType());
            appendParams(typeArg, type, 0);
            create.then(typeArg);
        }

        return create;
    }

    private static ArgumentBuilder<CommandSourceStack, ?> appendParams(ArgumentBuilder<CommandSourceStack, ?> parent, WorldEventType type, int index) {
        if (index >= type.params.length) {
            return parent.executes(ctx -> {
                Object[] args = new Object[type.params.length];
                for (int i = 0; i < type.params.length; i++) {
                    var p = type.params[i];
                    args[i] = p.getValue(ctx);

                }

                WorldEventInstance instance = createEventInstance(type, args);
                Level level = ctx.getSource().getLevel();
                WorldEventHandler.addWorldEvent(level, instance);
                return 1;
            });
        }

        parent.then(appendParams(type.params[index].toArg(), type, index + 1));
        return parent;
    }


    private static WorldEventInstance createEventInstance(WorldEventType type, Object[] args) {
        try {
            Class<? extends WorldEventInstance> clazz = type.supplier.getInstance().getClass();
            var constructor = clazz.getConstructor(Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new));
            return constructor.newInstance(args);
        }  catch (NoSuchMethodException e) {
            LodestoneLib.LOGGER.error("No matching constructor found for WorldEventInstance of type {} with the parameters: {}", type.id, Arrays.stream(args).map(Object::getClass).toList(), e);
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            LodestoneLib.LOGGER.error("Failed to create WorldEventInstance of type: {}", type.id, e);
            throw new RuntimeException(e);
        }
    }
}
