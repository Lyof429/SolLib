package net.lcc.sollib.mixin.common.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(GsonHelper.class)
public class GsonHelperMixin {
    @Unique private static <T> void sol_apply(JsonObject obj, String member, T fallback, Consumer<JsonElement> action) {
        if (!obj.has(member)) return;

        JsonElement json = obj.get(member);
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String key = json.getAsString();
            if (key.matches("config@[a-z_/]+:[a-z_.]+")) {

                key = key.substring(7);
                JsonElement value = SolRegistries.CONFIG.getRaw(key, fallback);
                if (value != null) action.accept(value);
            }
        }
    }

    @Inject(
            method = "getAsBigDecimal(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/math/BigDecimal;",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredBigDecimal(JsonObject json, String memberName, CallbackInfoReturnable<BigDecimal> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsBigDecimal());
        });
    }

    @Inject(
            method = "getAsBigInteger(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/math/BigInteger;",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredBigInteger(JsonObject json, String memberName, CallbackInfoReturnable<BigInteger> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsBigInteger());
        });
    }

    @Inject(
            method = "getAsBoolean(Lcom/google/gson/JsonObject;Ljava/lang/String;)Z",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredBoolean(JsonObject json, String memberName, CallbackInfoReturnable<Boolean> cir) {
        sol_apply(json, memberName, true, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isBoolean())
                cir.setReturnValue(v.getAsBoolean());
        });
    }

    @Inject(
            method = "getAsByte(Lcom/google/gson/JsonObject;Ljava/lang/String;)B",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredByte(JsonObject json, String memberName, CallbackInfoReturnable<Byte> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsByte());
        });
    }

    @Inject(
            method = "getAsCharacter(Lcom/google/gson/JsonObject;Ljava/lang/String;)C",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredCharacter(JsonObject json, String memberName, CallbackInfoReturnable<Character> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsCharacter());
        });
    }

    @Inject(
            method = "getAsDouble(Lcom/google/gson/JsonObject;Ljava/lang/String;)D",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredDouble(JsonObject json, String memberName, CallbackInfoReturnable<Double> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsDouble());
        });
    }

    @Inject(
            method = "getAsFloat(Lcom/google/gson/JsonObject;Ljava/lang/String;)F",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredFloat(JsonObject json, String memberName, CallbackInfoReturnable<Float> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsFloat());
        });
    }

    @Inject(
            method = "getAsInt(Lcom/google/gson/JsonObject;Ljava/lang/String;)I",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredInt(JsonObject json, String memberName, CallbackInfoReturnable<Integer> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsInt());
        });
    }

    @Inject(
            method = "getAsItem(Lcom/google/gson/JsonObject;Ljava/lang/String;)Lnet/minecraft/world/item/Item;",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredItem(JsonObject json, String memberName, CallbackInfoReturnable<Item> cir) {
        sol_apply(json, memberName, "", v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isString())
                cir.setReturnValue(BuiltInRegistries.ITEM.getOptional(new ResourceLocation(v.getAsString())).orElseThrow(
                        () -> new JsonSyntaxException("Expected " + memberName + " to be an item, was unknown string '" + v + "'")));
        });
    }

    @Inject(
            method = "getAsJsonArray(Lcom/google/gson/JsonObject;Ljava/lang/String;)Lcom/google/gson/JsonArray;",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredArray(JsonObject json, String memberName, CallbackInfoReturnable<JsonArray> cir) {
        sol_apply(json, memberName, new JsonArray(), v -> {
            if (v.isJsonArray())
                cir.setReturnValue(v.getAsJsonArray());
        });
    }

    @Inject(
            method = "getAsJsonObject(Lcom/google/gson/JsonObject;Ljava/lang/String;)Lcom/google/gson/JsonObject;",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredObject(JsonObject json, String memberName, CallbackInfoReturnable<JsonObject> cir) {
        sol_apply(json, memberName, new JsonObject(), v -> {
            if (v.isJsonObject())
                cir.setReturnValue(v.getAsJsonObject());
        });
    }

    @Inject(
            method = "getAsLong(Lcom/google/gson/JsonObject;Ljava/lang/String;)J",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredLong(JsonObject json, String memberName, CallbackInfoReturnable<Long> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsLong());
        });
    }

    @Inject(
            method = "getAsShort(Lcom/google/gson/JsonObject;Ljava/lang/String;)S",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredShort(JsonObject json, String memberName, CallbackInfoReturnable<Short> cir) {
        sol_apply(json, memberName, 0, v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isNumber())
                cir.setReturnValue(v.getAsShort());
        });
    }

    @Inject(
            method = "getAsString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;",
            at = @At("HEAD"), cancellable = true
    )
    private static void getConfiguredString(JsonObject json, String memberName, CallbackInfoReturnable<String> cir) {
        sol_apply(json, memberName, "", v -> {
            if (v.isJsonPrimitive() && v.getAsJsonPrimitive().isString())
                cir.setReturnValue(v.getAsString());
        });
    }
}
