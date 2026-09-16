package net.lcc.sollib.api.event;

import net.lcc.sollib.api.common.data.reload.SReloadRegistry;
import net.lcc.sollib.api.common.logger.SolLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SEventType<E> {
    private static final SolLogger LOG = new SolLogger("Sol/Event");

    private final List<Consumer<E>> subscribers = new ArrayList<>();
    private final Function<SEventListener, Consumer<E>> converter;

    public SEventType(Function<SEventListener, Consumer<E>> converter) {
        this.converter = converter;
    }

    protected void addListener(SEventListener listener) {
        this.subscribers.add(this.converter.apply(listener));
    }

    public void emit(E event) {
        this.subscribers.forEach(sub -> {
            try {
                sub.accept(event);
            } catch (Exception e) {
                LOG.error(sub, event, ": Error while running event", e);
            }
        });
    }
}
