package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.data.SpawnRestrictions;
import net.lcc.sollib.api.common.registry.data.SpawnRules;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EntityHolder extends Holder<EntityType<?>> {
    private List<TagKey<EntityType<?>>> tags;
    private Supplier<LootTable.Builder> drop;
    private Supplier<AttributeSupplier.Builder> attributesBuilder;
    private SpawnRestrictions<?> spawnRestrictions;
    private SpawnRules spawnRules;
    // Client
    private EntityRendererProvider<?> renderer = null;
    private final Map<ModelLayerLocation, Supplier<LayerDefinition>> layers = new HashMap<>();

    public EntityHolder(SolModContainer mod, String name, Supplier<EntityType<?>> entrySupplier) {
        super(mod, name, entrySupplier);
        this.tags = List.of();
        this.drop = null;
        this.attributesBuilder = null;
        this.spawnRestrictions = null;
        this.spawnRules = null;
    }

    @Override
    protected Registry<EntityType<?>> getRegistry() {
        return BuiltInRegistries.ENTITY_TYPE;
    }

    /**
     * Registers this entity to the supplied tags
     *
     * @param tags the tag keys to register the entity to
     */
    @SafeVarargs
    public final EntityHolder withTags(TagKey<EntityType<?>>... tags) {
        this.tags = List.of(tags);
        return this;
    }

    public List<TagKey<EntityType<?>>> getTags() {
        return this.tags;
    }


    public Supplier<LootTable.Builder> getDrop() {
        return this.drop;
    }

    public final EntityHolder drop(Supplier<LootTable.Builder> drop) {
        this.drop = drop;
        return this;
    }

    public boolean hasDrop() {
        return this.drop != null;
    }

    public EntityHolder withAttributes(Supplier<AttributeSupplier.Builder> builder) {
        this.attributesBuilder = builder;
        return this;
    }

    public AttributeSupplier.Builder getAttributes() {
        return this.attributesBuilder.get();
    }

    public boolean hasAttributes() {
        return this.attributesBuilder != null;
    }

    public <T extends Mob> EntityHolder withSpawnRestrictions(SpawnPlacements.Type location, Heightmap.Types heightmap,
                                                                 SpawnPlacements.SpawnPredicate<T> predicate) {
        this.spawnRestrictions = new SpawnRestrictions<>(location, heightmap, predicate);
        return this;
    }

    public <T extends Mob> SpawnRestrictions<T> getSpawnRestrictions() {
        return (SpawnRestrictions<T>) this.spawnRestrictions;
    }

    public boolean hasSpawnRestrictions() {
        return this.spawnRestrictions != null;
    }

    public EntityHolder withSpawn(List<String> biomes, MobCategory category, int weight, int min, int max) {
        this.spawnRules = SpawnRules.create(biomes, category, weight, min, max);
        return this;
    }

    public EntityHolder withSpawn(List<ResourceKey<Biome>> biomeKeys, List<TagKey<Biome>> biomeTags,
                                  MobCategory category, int weight, int min, int max) {
        this.spawnRules = new SpawnRules(biomeKeys, biomeTags, category, weight, min, max);
        return this;
    }

    public SpawnRules getSpawn() {
        return this.spawnRules;
    }

    public boolean shouldSpawn() {
        return this.spawnRules != null;
    }

    // Client
    /**
     * Registers the renderer for this entity type. <br/>
     * <b>Must only be called on the client</b>
     */
    public <T extends Entity> EntityHolder addRenderer(EntityRendererProvider<T> renderer) {
        this.renderer = renderer;
        return this;
    }

    public <T extends Entity> EntityRendererProvider<T> getRenderer() {
        return (EntityRendererProvider<T>) this.renderer;
    }

    public boolean hasRenderer() {
        return this.renderer != null;
    }

    /**
     * Registers a layer location for this entity type. <br/>
     * <b>Must only be called on the client</b>
     */
    public EntityHolder addModelLayer(ModelLayerLocation location, Supplier<LayerDefinition> layer) {
        this.layers.putIfAbsent(location, layer);
        return this;
    }

    public Iterable<Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>>> getModelLayers() {
        return this.layers.entrySet();
    }
}
