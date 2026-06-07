package net.lcc.sollib.api.common.worldgen.density;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lcc.sollib.core.Identifier;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DensityFunction} implementation that dynamically changes with connected players' advancements <br/>
 * Syntax is that, with as many items as wanted in points:
 * <pre>
 * {
 *   "type": "sollib:progression",
 *   "points": [
 *     {
 *       "value": -1,
 *       "advancement": "minecraft:story/root"
 *     },
 *     {
 *       "value": 1,
 *       "advancement": "minecraft:end/kill_dragon"
 *     }
 *   ]
 * }
 * </pre>
 */
public class ProgressionDensityFunction implements DensityFunction.SimpleFunction {
    public record ProgressionPoint(float value, ResourceLocation advancement) {}

    public static final MapCodec<ProgressionPoint> POINT_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("value").forGetter(ProgressionPoint::value),
                    ExtraCodecs.NON_EMPTY_STRING.fieldOf("advancement").xmap(Identifier::of, ResourceLocation::toString)
                            .forGetter(ProgressionPoint::advancement)
            ).apply(instance, ProgressionPoint::new));
    public static final MapCodec<ProgressionDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("default_value").forGetter(ProgressionDensityFunction::getDefaultValue),
                    ExtraCodecs.nonEmptyList(POINT_CODEC.codec().listOf()).fieldOf("points")
                            .forGetter(ProgressionDensityFunction::getPoints)
            ).apply(instance, ProgressionDensityFunction::new));
    private static final KeyDispatchDataCodec<ProgressionDensityFunction> CODEC_HOLDER = KeyDispatchDataCodec.of(CODEC);

    private static final List<ProgressionDensityFunction> INSTANCES = new ArrayList<>();

    protected final List<ProgressionPoint> points;
    protected final float defaultValue;
    protected float min;
    protected float max;

    protected float value;

    public ProgressionDensityFunction(float defaultValue, List<ProgressionPoint> points) {
        this.defaultValue = defaultValue;
        this.value = this.defaultValue;
        this.points = points;

        this.min = points.get(0).value();
        this.max = points.get(0).value();
        for (ProgressionPoint point : this.points) {
            if (point.value() < this.min) this.min = point.value();
            else if (point.value() > this.max) this.max = point.value();
        }

        INSTANCES.add(this);
    }

    public List<ProgressionPoint> getPoints() {
        return this.points;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    @ApiStatus.Internal
    public static void reloadAll(MinecraftServer server) {
        for (ProgressionDensityFunction density : INSTANCES)
            density.reload(server);
    }

    @ApiStatus.Internal
    protected void reload(MinecraftServer server) {
        this.value = this.defaultValue;
        if (server == null) return;

        AdvancementHolder advancement;
        iterate : for (ProgressionPoint point : this.points) {
            advancement = server.getAdvancements().get(point.advancement());
            if (advancement == null)
                continue;

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    this.value = point.value();
                    continue iterate;
                }
            }
        }
    }

    @Override
    public double compute(FunctionContext context) {
        return this.value;
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
