package net.lcc.sollib.api.common.logger;

import net.lcc.sollib.platform.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolLogger {
    private final Logger logger;
    private final String name;
    private String last = "";

    /**
     * @param name The logger's name
     */
    public SolLogger(String name) {
         this.logger = LoggerFactory.getLogger(name.split("/")[0]);
         this.name = name;
    }

    @SafeVarargs
    private <T, E> String build(T message, E... extra) {
        StringBuilder builder = new StringBuilder("[" + this.name + "] " + message);
        for (E e : extra)
            builder.append(" ").append(e);
        return builder.toString();
    }

    /**
     * Displays a blank space between each value
     * @param message The first value to log, which will be returned
     * @param extra Any number of optional arguments to be logged alongside
     * @return The first parameter
     */
    @SafeVarargs
    public final <T, E> T info(T message, E... extra) {
        this.logger.info(this.build(message, extra));
        return message;
    }

    /**
     * Displays a blank space between each value
     * @param message The first value to log, which will be returned
     * @param extra Any number of optional arguments to be logged alongside
     * @return The first parameter
     */
    @SafeVarargs
    public final <T, E> T warn(T message, E... extra) {
        this.logger.warn(this.build(message, extra));
        return message;
    }

    /**
     * Displays a blank space between each value
     * @param message The first value to log, which will be returned
     * @param extra Any number of optional arguments to be logged alongside
     * @return The first parameter
     */
    @SafeVarargs
    public final <T, E> T error(T message, E... extra) {
        this.logger.error(this.build(message, extra));
        return message;
    }

    /**
     * Displays a blank space between each value
     * @param message The first value to log, which will be returned
     * @param extra Any number of optional arguments to be logged alongside
     * @return The first parameter
     */
    @SafeVarargs
    public final <T, E> T debug(T message, E... extra) {
        if (Services.PLATFORM != null && Services.PLATFORM.isDevelopmentEnvironment()) {
            String m = this.build(message);
            if (!this.last.equals(m))
                this.logger.warn(m);
            this.last = m;
        }
        return message;
    }
}
