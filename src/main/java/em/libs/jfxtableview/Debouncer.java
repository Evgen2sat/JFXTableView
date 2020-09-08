package em.libs.jfxtableview;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Первое событие запоминает и передает обработчику после таймаута,
 * остальные события игнорирует, пока первое событие не будет передано
 */
public class Debouncer<T extends Event> implements EventHandler<T> {
    private final EventHandler<T> handler;
    private final long msTimeout;
    private T event;
    private CompletableFuture<Void> delayedEventHandler;

    public Debouncer(long msTimeout, EventHandler<T> handler) {
        this.msTimeout = msTimeout;
        this.handler = handler;
    }

    @Override
    public void handle(T event) {
        if(delayedEventHandler == null) {
            this.event = event;

            delayedEventHandler = CompletableFuture.runAsync(() ->
                    Platform.runLater(() -> {
                        handler.handle(this.event);
                        delayedEventHandler = null;
                    }),
                    CompletableFuture.delayedExecutor(msTimeout, TimeUnit.MILLISECONDS));
        }
    }
}
