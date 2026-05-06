package net.lcc.sollib.api.common.worldgen.density;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.List;

public class ProgressionDensityFunction implements DensityFunction.SimpleFunction {
    public record ProgressionPoint(float value, ResourceLocation advancement) {}

    public static final Codec<ProgressionPoint> POINT_CODEC = RecordCodecBuilder.create(point ->
            point.group(
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("value").forGetter(ProgressionPoint::value),
                    ExtraCodecs.NON_EMPTY_STRING.fieldOf("advancement").xmap(it -> new ResourceLocation(it), ResourceLocation::toString)
                            .forGetter(ProgressionPoint::advancement)
            ).apply(point, ProgressionPoint::new));
    public static final Codec<ProgressionDensityFunction> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ExtraCodecs.nonEmptyList(POINT_CODEC.listOf()).fieldOf("points").forGetter(ProgressionDensityFunction::getPoints)
            ).apply(instance, ProgressionDensityFunction::new));
    public static final KeyDispatchDataCodec<ProgressionDensityFunction> CODEC_HOLDER = KeyDispatchDataCodec.of(CODEC);

    protected final List<ProgressionPoint> points;
    protected float min;
    protected float max;

    public ProgressionDensityFunction(List<ProgressionPoint> points) {
        this.points = points;

        this.min = points.get(0).value();
        this.max = points.get(0).value();
        for (ProgressionPoint point : this.points) {
            if (point.value() < this.min) this.min = point.value();
            else if (point.value() > this.max) this.max = point.value();
        }
    }

    public List<ProgressionPoint> getPoints() {
        return this.points;
    }

    @Override
    public double compute(FunctionContext context) {
        return 0;
    }

    @Override
    public double minValue() {
        return this.min;
    }

    @Override
    public double maxValue() {
        return this.max;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC_HOLDER;
    }
}
