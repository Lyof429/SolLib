package net.lcc.sollib.api.common.config;

/**
 * Technically not fully a FunctionalInterface, but marking it as one because it is used this way for code cleaning <br/>
 * Can be implemented by any other class to make it be easily converted to JSON using {@link JsonBuilder#addCategory(String, IConfigurable)}
 */
@FunctionalInterface
public interface IConfigurable {
    void toJson(JsonBuilder builder);
}
