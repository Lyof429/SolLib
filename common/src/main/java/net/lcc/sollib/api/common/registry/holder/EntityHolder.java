package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.function.Supplier;

public class EntityHolder extends Holder<EntityType<?>> {
    private List<TagKey<EntityType<?>>> tags;
    private Supplier<LootTable.Builder> drop;
    private AttributeSupplier.Builder attributesBuilder;

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

    /* Explodes because the class doesn't exist on servers...
    public <T extends Entity> EntityHolder withRenderer(EntityRendererProvider<T> renderer) {
        this.renderer = renderer;
        return this;
    }
     */
}
