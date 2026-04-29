package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EntityHolder extends Holder<EntityType<?>> {
    private List<TagKey<EntityType<?>>> tags;
    private Supplier<LootTable.Builder> drop;
    private AttributeSupplier.Builder attributesBuilder;
    // Client
    private EntityRendererProvider<?> renderer = null;
    private final Map<ModelLayerLocation, Supplier<LayerDefinition>> layers = new HashMap<>();

    public EntityHolder(SolModContainer mod, String name, Supplier<EntityType<?>> entrySupplier) {
        super(mod, name, entrySupplier);
        this.tags = List.of();
        this.drop = null;
        this.attributesBuilder = null;
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

    public EntityHolder withAttributes(AttributeSupplier.Builder builder) {
        this.attributesBuilder = builder;
        return this;
    }

    public AttributeSupplier getAttributes() {
        return this.attributesBuilder.build();
    }

    public boolean hasAttributes() {
        return this.attributesBuilder != null;
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
