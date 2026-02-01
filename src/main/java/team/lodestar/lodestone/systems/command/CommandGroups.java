package team.lodestar.lodestone.systems.command;

import com.mojang.datafixers.util.*;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class CommandGroups {

    public static final class Group1<O, A> {
        private final CommandField<?, A> a;
        Group1(CommandField<?, A> a) { this.a = a; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function<A, O> ctor) {
            return new CompositeCodecs.CompositeCodec1<>(a, ctor);
        }
    }

    public static final class Group2<O, A, B> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        Group2(CommandField<?, A> a, CommandField<?, B> b) { this.a = a; this.b = b; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, BiFunction<A, B, O> ctor) {
            return new CompositeCodecs.CompositeCodec2<>(a, b, ctor);
        }
    }

    public static final class Group3<O, A, B, C> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        Group3(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c) { this.a = a; this.b = b; this.c = c; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function3<A, B, C, O> ctor) {
            return new CompositeCodecs.CompositeCodec3<>(a, b, c, ctor);
        }
    }

    public static final class Group4<O, A, B, C, D> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        Group4(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d) { this.a = a; this.b = b; this.c = c; this.d = d; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function4<A, B, C, D, O> ctor) {
            return new CompositeCodecs.CompositeCodec4<>(a, b, c, d, ctor);
        }
    }

    public static final class Group5<O, A, B, C, D, E> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        Group5(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function5<A, B, C, D, E, O> ctor) {
            return new CompositeCodecs.CompositeCodec5<>(a, b, c, d, e, ctor);
        }
    }

    public static final class Group6<O, A, B, C, D, E, F> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        Group6(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function6<A, B, C, D, E, F, O> ctor) {
            return new CompositeCodecs.CompositeCodec6<>(a, b, c, d, e, f, ctor);
        }
    }

    public static final class Group7<O, A, B, C, D, E, F, G> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        Group7(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function7<A, B, C, D, E, F, G, O> ctor) {
            return new CompositeCodecs.CompositeCodec7<>(a, b, c, d, e, f, g, ctor);
        }
    }

    public static final class Group8<O, A, B, C, D, E, F, G, H> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        Group8(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function8<A, B, C, D, E, F, G, H, O> ctor) {
            return new CompositeCodecs.CompositeCodec8<>(a, b, c, d, e, f, g, h, ctor);
        }
    }
}
