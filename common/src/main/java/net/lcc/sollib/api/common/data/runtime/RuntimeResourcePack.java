package net.lcc.sollib.api.common.data.runtime;

import net.lcc.sollib.SolLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class RuntimeResourcePack implements PackResources {
    protected static final RuntimeResourcePack INSTANCE = new RuntimeResourcePack();

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... strings) {
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        return null;
    }

    @Override
    public void listResources(PackType packType, String s, String s1, ResourceOutput resourceOutput) {

    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        return Set.of();
    }

    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {
        return null;
    }

    @Override
    public String packId() {
        return SolLib.MOD_ID + "_runtime_data";
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public void close() {

    }
}
