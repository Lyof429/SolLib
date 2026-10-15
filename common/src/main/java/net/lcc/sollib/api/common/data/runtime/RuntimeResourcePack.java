package net.lcc.sollib.api.common.data.runtime;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

public class RuntimeResourcePack implements PackResources {
    public static final RuntimeResourcePack INSTANCE = new RuntimeResourcePack();

    private static final PackLocationInfo LOCATION = new PackLocationInfo(SolLib.MOD_ID + "_runtime_data",
            Component.literal("Sol Runtime Data"), PackSource.DEFAULT, Optional.empty());

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... elements) {
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType type, ResourceLocation id) {
        if (!SolRegistries.Data.RUNTIME.provides(id)) return null;
        Resource resource = SolRegistries.Data.RUNTIME.apply(id, null);
        return resource == null ? null : resource::open;
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput resourceOutput) {
        for (ResourceLocation id : SolRegistries.Data.RUNTIME.findMatching(namespace, path))
            resourceOutput.accept(id, () -> open(id));
    }

    private static InputStream open(ResourceLocation id) throws IOException {
        Resource resource = SolRegistries.Data.RUNTIME.apply(id, null);
        if (resource == null) throw new FileNotFoundException(id.toString());
        return resource.open();
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return SolRegistries.Data.RUNTIME.getNamespaces();
    }

    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) throws IOException {
        return null;
    }

    @Override
    public PackLocationInfo location() {
        return LOCATION;
    }

    @Override
    public void close() {
    }
}
