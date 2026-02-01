package team.lodestar.lodestone.systems.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.*;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class CompositeCodecs {

    public static class CompositeCodec1<O, A> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        private final Function<A, O> constructor;

        public CompositeCodec1(CommandField<?, A> a, Function<A, O> constructor) {
            this.a = a;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = (ArgumentBuilder<CommandSourceStack, ?>) a.getArgument();

            argA.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec2<O, A, B> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        private final BiFunction<A, B, O> constructor;

        public CompositeCodec2(CommandField<?, A> a, CommandField<?, B> b, BiFunction<A, B, O> constructor) {
            this.a = a;
            this.b = b;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<net.minecraft.commands.CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = (ArgumentBuilder<CommandSourceStack, ?>) a.getArgument();
            var argB = (ArgumentBuilder<CommandSourceStack, ?>) b.getArgument();

            argB.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec3<O, A, B, C> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        private final Function3<A, B, C, O> constructor;

        public CompositeCodec3(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, Function3<A, B, C, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = (ArgumentBuilder<CommandSourceStack, ?>) a.getArgument();
            var argB = (ArgumentBuilder<CommandSourceStack, ?>) b.getArgument();
            var argC = (ArgumentBuilder<CommandSourceStack, ?>) c.getArgument();

            argC.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec4<O, A, B, C, D> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        private final Function4<A, B, C, D, O> constructor;

        public CompositeCodec4(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, Function4<A, B, C, D, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = (ArgumentBuilder<CommandSourceStack, ?>) a.getArgument();
            var argB = (ArgumentBuilder<CommandSourceStack, ?>) b.getArgument();
            var argC = (ArgumentBuilder<CommandSourceStack, ?>) c.getArgument();
            var argD = (ArgumentBuilder<CommandSourceStack, ?>) d.getArgument();

            argD.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec5<O, A, B, C, D, E> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        private final Function5<A, B, C, D, E, O> constructor;

        public CompositeCodec5(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, Function5<A, B, C, D, E, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = (ArgumentBuilder<CommandSourceStack, ?>) a.getArgument();
            var argB = (ArgumentBuilder<CommandSourceStack, ?>) b.getArgument();
            var argC = (ArgumentBuilder<CommandSourceStack, ?>) c.getArgument();
            var argD = (ArgumentBuilder<CommandSourceStack, ?>) d.getArgument();
            var argE = (ArgumentBuilder<CommandSourceStack, ?>) e.getArgument();

            argE.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec6<O, A, B, C, D, E, F> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        private final Function6<A, B, C, D, E, F, O> constructor;

        public CompositeCodec6(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, Function6<A, B, C, D, E, F, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = (ArgumentBuilder<CommandSourceStack, ?>) a.getArgument();
            var argB = (ArgumentBuilder<CommandSourceStack, ?>) b.getArgument();
            var argC = (ArgumentBuilder<CommandSourceStack, ?>) c.getArgument();
            var argD = (ArgumentBuilder<CommandSourceStack, ?>) d.getArgument();
            var argE = (ArgumentBuilder<CommandSourceStack, ?>) e.getArgument();
            var argF = (ArgumentBuilder<CommandSourceStack, ?>) f.getArgument();

            argF.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec7<O, A, B, C, D, E, F, G> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        private final Function7<A, B, C, D, E, F, G, O> constructor;

        public CompositeCodec7(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, Function7<A, B, C, D, E, F, G, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = (ArgumentBuilder<CommandSourceStack, ?>) a.getArgument();
            var argB = (ArgumentBuilder<CommandSourceStack, ?>) b.getArgument();
            var argC = (ArgumentBuilder<CommandSourceStack, ?>) c.getArgument();
            var argD = (ArgumentBuilder<CommandSourceStack, ?>) d.getArgument();
            var argE = (ArgumentBuilder<CommandSourceStack, ?>) e.getArgument();
            var argF = (ArgumentBuilder<CommandSourceStack, ?>) f.getArgument();
            var argG = (ArgumentBuilder<CommandSourceStack, ?>) g.getArgument();

            argG.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec8<O, A, B, C, D, E, F, G, H> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        private final Function8<A, B, C, D, E, F, G, H, O> constructor;

        public CompositeCodec8(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, Function8<A, B, C, D, E, F, G, H, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = (ArgumentBuilder<CommandSourceStack, ?>) a.getArgument();
            var argB = (ArgumentBuilder<CommandSourceStack, ?>) b.getArgument();
            var argC = (ArgumentBuilder<CommandSourceStack, ?>) c.getArgument();
            var argD = (ArgumentBuilder<CommandSourceStack, ?>) d.getArgument();
            var argE = (ArgumentBuilder<CommandSourceStack, ?>) e.getArgument();
            var argF = (ArgumentBuilder<CommandSourceStack, ?>) f.getArgument();
            var argG = (ArgumentBuilder<CommandSourceStack, ?>) g.getArgument();
            var argH = (ArgumentBuilder<CommandSourceStack, ?>) h.getArgument();

            argH.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }
}
