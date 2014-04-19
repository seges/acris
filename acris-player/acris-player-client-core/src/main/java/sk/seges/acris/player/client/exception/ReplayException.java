package sk.seges.acris.player.client.exception;

import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

/**
 * Created by PeterSimun on 18.4.2014.
 */
public class ReplayException extends RuntimeException {

    @SuppressWarnings("GwtInconsistentSerializableClass")
    protected AbstractGenericEvent event;

    protected ReplayException() {}

    public ReplayException(AbstractGenericEvent event) {
        super();
        this.event = event;
    }

    public ReplayException(String message, AbstractGenericEvent event) {
        super(message);
        this.event = event;
    }

    public ReplayException(String message, Throwable cause, AbstractGenericEvent event) {
        super(message, cause);
        this.event = event;
    }

    public ReplayException(Throwable cause, AbstractGenericEvent event) {
        super(cause);
        this.event = event;
    }
}