package team.lodestar.lodestone.systems.particle.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.base.TextureSheetScreenParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.Consumer;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.*;

public class LodestoneScreenParticle extends TextureSheetScreenParticle {
    private final LodestoneScreenParticleRenderType renderType;
    protected final ParticleEngine.MutableSpriteSet spriteSet;
    protected final SimpleParticleOptions.ParticleSpritePicker spritePicker;
    protected final ColorParticleData colorData;
    protected final GenericParticleData transparencyData;
    protected final GenericParticleData scaleData;
    @Nullable
    protected final GenericParticleData lengthData;
    protected final SpinParticleData spinData;
    protected final Consumer<LodestoneScreenParticle> actor;
    private final boolean tracksStack;
    private final double stackTrackXOffset;
    private final double stackTrackYOffset;

    private int lifeDelay;

    private float quadLength;

    float[] hsv1 = new float[3], hsv2 = new float[3];

    public LodestoneScreenParticle(ClientLevel world, ScreenParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double xMotion, double yMotion) {
        super(world, x, y);
        this.renderType = options.renderType;
        this.spriteSet = spriteSet;
        this.spritePicker = options.spritePicker;
        this.colorData = options.colorData;
        this.transparencyData = options.transparencyData;
        this.scaleData = options.scaleData;
        this.lengthData = options.lengthData != WorldParticleOptions.DEFAULT_GENERIC ? options.lengthData : null;
        this.spinData = options.spinData;
        this.actor = options.actor;
        this.tracksStack = options.tracksStack;
        this.stackTrackXOffset = options.stackTrackXOffset;
        this.stackTrackYOffset = options.stackTrackYOffset;
        this.roll = options.spinData.spinOffset + options.spinData.startingValue;
        this.xMotion = xMotion;
        this.yMotion = yMotion;

        this.setLifetime(options.lifetimeSupplier.get());
        this.lifeDelay = options.lifeDelaySupplier.get();
        this.gravity = options.gravityStrengthSupplier.get();
        this.friction = options.frictionStrengthSupplier.get();
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r1)), (int) (255 * Math.min(1.0f, colorData.g1)), (int) (255 * Math.min(1.0f, colorData.b1)), hsv1);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r2)), (int) (255 * Math.min(1.0f, colorData.g2)), (int) (255 * Math.min(1.0f, colorData.b2)), hsv2);
        if (spriteSet != null) {
            switch (spritePicker) {
                case FIRST_INDEX, WITH_AGE -> pickSprite(0);
                case LAST_INDEX, WITH_AGE_INVERSE -> pickSprite(spriteSet.sprites.size() - 1);
                case RANDOM_SPRITE -> pickSprite(random.nextInt(spriteSet.sprites.size()));
            }
        }
        updateTraits();
    }

    @Override
    public float getQuadLength(float partialTicks) {
        return lengthData != null ? quadLength : super.getQuadLength(partialTicks);
    }

    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return spritePicker;
    }

    public void pickSprite(int spriteIndex) {
        setSprite(spriteSet.sprites.get(Mth.clamp(spriteIndex, 0, spriteSet.sprites.size() - 1)));
    }

    public void pickColor(float colorCoeff) {
        float h = Mth.rotLerp(colorCoeff, 360f * hsv1[0], 360f * hsv2[0]) / 360f;
        float s = Mth.lerp(colorCoeff, hsv1[1], hsv2[1]);
        float v = Mth.lerp(colorCoeff, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = FastColor.ARGB32.red(packed) / 255.0f;
        float g = FastColor.ARGB32.green(packed) / 255.0f;
        float b = FastColor.ARGB32.blue(packed) / 255.0f;
        setColor(r, g, b);
    }

    public float getCurve(float multiplier) {
        return Mth.clamp((age * multiplier) / (float) lifetime, 0, 1);
    }

    protected void updateTraits() {
        if (scaleData.getProgress(age, lifetime) > 0.8f || transparencyData.getProgress(age, lifetime) > 0.8f) {
            if (alpha <= 0 || getQuadSize(0) <= 0 || getQuadLength(0) <= 0) {
                remove();
            }
            return;
        }

        pickColor(colorData.colorCurveEasing.ease(colorData.getProgress(age, lifetime), 0, 1, 1));

        quadSize = scaleData.getValue(age, lifetime);
        quadLength = lengthData != null ? lengthData.getValue(age, lifetime) : quadSize;

        alpha = Mth.clamp(transparencyData.getValue(age, lifetime), 0, 1);
        oRoll = roll;
        roll += spinData.getValue(age, lifetime);

        if (actor != null) {
            actor.accept(this);
        }
    }

    @Override
    public void render(BufferBuilder bufferBuilder) {
        if (lifeDelay > 0) {
            return;
        }
        if (tracksStack) {
            x = ScreenParticleHandler.currentItemX + stackTrackXOffset + xMoved;
            y = ScreenParticleHandler.currentItemY + stackTrackYOffset + yMoved;
        }
        super.render(bufferBuilder);
    }

    @Override
    public void tick() {
        if (lifeDelay > 0) {
            lifeDelay--;
            return;
        }
        updateTraits();
        super.tick();
        if (spriteSet != null) {
            switch (spritePicker) {
                case WITH_AGE -> setSpriteFromAge(spriteSet);
                case WITH_AGE_INVERSE -> setSpriteFromInverseAge(spriteSet);
            }
        }
    }

    @Override
    public LodestoneScreenParticleRenderType getRenderType() {
        return renderType;
    }

    public void setSpriteFromInverseAge(SpriteSet sprite) {
        if (!this.removed) {
            this.setSprite(sprite.get(lifetime - age, lifetime));
        }
    }

    public void setParticleSpeed(Vector3d speed) {
        setParticleSpeed(speed.x, speed.y);
    }
}