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
         this.logger = LoggerFactory.getLogger(name);
         this.name = name;
    }

    @SafeVarargs
    private <T> String build(T... message) {
        StringBuilder builder = new StringBuilder("[" + this.name + "]");
        for (T m : message)
            builder.append(" ").append(m);
        return builder.toString();
    }

    /**
     * Displays a blank space between each value
     * @param message Any number of values to log
     * @return The first parameter
     */
    @SafeVarargs
    public final <T> T info(T... message) {
        this.logger.info(this.build(message));
        return message[0];
    }

    /**
     * Displays a blank space between each value
     * @param message Any number of values to log
     * @return The first parameter
     */
    @SafeVarargs
    public final <T> T warn(T... message) {
        this.logger.warn(this.build(message));
        return message[0];
    }

    /**
     * Displays a blank space between each value
     * @param message Any number of values to log
     * @return The first parameter
     */
    @SafeVarargs
    public final <T> T error(T... message) {
        this.logger.error(this.build(message));
        return message[0];
    }

    /**
     * Displays a blank space between each value <br/>
     * Ignores consecutive identical logs, and only fires in development environment
     * @param message Any number of values to log
     * @return The first parameter
     */
    @SafeVarargs
    public final <T> T debug(T... message) {
        if (Services.PLATFORM != null && Services.PLATFORM.isDevelopmentEnvironment()) {
            String m = this.build(message);
            if (!this.last.equals(m))
                this.logger.warn(m);
            this.last = m;
        }
        return message[0];
    }
}
