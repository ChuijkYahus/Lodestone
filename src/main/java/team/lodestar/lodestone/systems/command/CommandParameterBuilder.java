package team.lodestar.lodestone.systems.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class CommandParameterBuilder {
    private final List<DynamicCommandParameter<?, ?>> params = new ArrayList<>();

    public static CommandParameterBuilder create() {
        return new CommandParameterBuilder();
    }

    public <A, V> CommandParameterBuilder add(String name, ArgumentType<A> type, BiFunction<CommandContext<CommandSourceStack>, String, V> getter) {
        params.add(new DynamicCommandParameter<>(name, type, getter));
        return this;
    }

    public DynamicCommandParameter<?, ?>[] build() {
        return params.toArray(new DynamicCommandParameter<?, ?>[0]);
    }
}
