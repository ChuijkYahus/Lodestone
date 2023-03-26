package team.lodestar.lodestone.helpers.render;

import team.lodestar.lodestone.systems.easing.Easing;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.awt.*;
import java.util.List;

public class ColorHelper {

    public static Color colorLerp(Easing easing, float delta, Color startColor, Color endColor) {
        delta = Mth.clamp(delta, 0, 1);
        int startColorRed = startColor.getRed(), startColorGreen = startColor.getGreen(), startColorBlue = startColor.getBlue();
        int endColorRed = endColor.getRed(), endColorGreen = endColor.getGreen(), endColorBlue = endColor.getBlue();

        float ease = easing.ease(delta, 0, 1, 1);

        int red = (int) Mth.lerp(ease, startColorRed, endColorRed);
        int green = (int) Mth.lerp(ease, startColorGreen, endColorGreen);
        int blue = (int) Mth.lerp(ease, startColorBlue, endColorBlue);

        return new Color(Mth.clamp(red, 0, 255), Mth.clamp(green, 0, 255), Mth.clamp(blue, 0, 255));
    }

    public static Color colorLerp(Easing easing, float delta, float min, float max, Color startColor, Color endColor) {
        delta = Mth.clamp(delta, 0, 1);
        int startColorRed = startColor.getRed(), startColorGreen = startColor.getGreen(), startColorBlue = startColor.getBlue();
        int endColorRed = endColor.getRed(), endColorGreen = endColor.getGreen(), endColorBlue = endColor.getBlue();

        float ease = easing.ease(delta, min, max, 1);

        int red = (int) Mth.lerp(ease, startColorRed, endColorRed);
        int green = (int) Mth.lerp(ease, startColorGreen, endColorGreen);
        int blue = (int) Mth.lerp(ease, startColorBlue, endColorBlue);

        return new Color(Mth.clamp(red, 0, 255), Mth.clamp(green, 0, 255), Mth.clamp(blue, 0, 255));
    }

    public static Color multicolorLerp(Easing easing, float pct, float min, float max, Color... colors) {
        return multicolorLerp(easing, pct, min, max, List.of(colors));
    }

    public static Color multicolorLerp(Easing easing, float pct, float min, float max, List<Color> colors) {
        pct = Mth.clamp(pct, 0, 1);
        int colorCount = colors.size() - 1;
        float lerp = easing.ease(pct, 0, 1, 1);
        float colorIndex = colorCount * lerp;
        int index = (int) Mth.clamp(colorIndex, 0, colorCount);
        Color color = colors.get(index);
        Color nextColor = index == colorCount ? color : colors.get(index + 1);
        return ColorHelper.colorLerp(easing, colorIndex - (int) (colorIndex), min, max, nextColor, color);
    }

    public static Color multicolorLerp(Easing easing, float pct, Color... colors) {
        return multicolorLerp(easing, pct, List.of(colors));
    }

    public static Color multicolorLerp(Easing easing, float pct, List<Color> colors) {
        pct = Mth.clamp(pct, 0, 1);
        int colorCount = colors.size() - 1;
        float lerp = easing.ease(pct, 0, 1, 1);
        float colorIndex = colorCount * lerp;
        int index = (int) Mth.clamp(colorIndex, 0, colorCount);
        Color color = colors.get(index);
        Color nextColor = index == colorCount ? color : colors.get(index + 1);
        return ColorHelper.colorLerp(easing, colorIndex - (int) (colorIndex), color, nextColor);
    }

    public static void RGBToHSV(Color color, float[] hsv) {
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
    }

    public static Color darker(Color color, int times) {
        return darker(color, times, 0.7f);
    }

    public static Color darker(Color color, int power, float factor) {
        float FACTOR = (float) Math.pow(factor, power);
        return new Color(Math.max((int) (color.getRed() * FACTOR), 0),
                Math.max((int) (color.getGreen() * FACTOR), 0),
                Math.max((int) (color.getBlue() * FACTOR), 0),
                color.getAlpha());
    }

    public static Color brighter(Color color, int power) {
        return brighter(color, power, 0.7f);
    }

    public static Color brighter(Color color, int power, float factor) {
        float FACTOR = (float) Math.pow(factor, power);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();

        int i = (int) (1.0 / (1.0 - FACTOR));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;

        return new Color(Math.min((int) (r / FACTOR), 255),
                Math.min((int) (g / FACTOR), 255),
                Math.min((int) (b / FACTOR), 255),
                alpha);
    }
}