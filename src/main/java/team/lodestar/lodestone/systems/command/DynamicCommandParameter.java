package team.lodestar.lodestone.systems.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.function.BiFunction;

public class DynamicCommandParameter<A, V> {
    private final String name;
    private final ArgumentType<A> type;
    private final BiFunction<CommandContext<CommandSourceStack>, String, V> getter;

    public DynamicCommandParameter(String name, ArgumentType<A> type, BiFunction<CommandContext<CommandSourceStack>, String, V> getter) {
        this.name = name;
        this.type = type;
        this.getter = getter;
    }

    public String getName() {
        return name;
    }

    public ArgumentType<A> getType() {
        return type;
    }

    public V getValue(CommandContext<CommandSourceStack> ctx) {
        return getter.apply(ctx, name);
    }

    public RequiredArgumentBuilder<CommandSourceStack, A> toArg() {
        return Commands.argument(name, type);
    }
}
