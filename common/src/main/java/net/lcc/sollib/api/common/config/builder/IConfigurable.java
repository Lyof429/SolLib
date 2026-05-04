package net.lcc.sollib.api.common.config.builder;

/**
 * Technically not fully a FunctionalInterface, but marking it as one because it is used this way for code cleaning <br/>
 * Can be implemented by any other class to make it be easily converted to JSON using {@link JsonBuilder#addObject(String, IConfigurable)}
 */
@FunctionalInterface
public interface IConfigurable {
    void toJson(IJsonBuilder builder);
}
