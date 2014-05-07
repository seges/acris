package sk.seges.acris.player.client.players.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class ControlEvent extends GwtEvent<ControlEvent.ControlEventHandler> {

    public enum ControlType {
        PLAY, PAUSE, NEXT, PREVIOUS, SPECIFIC, STOP;
    }

	private static final Type<ControlEventHandler> TYPE = new Type<ControlEventHandler>();

	private ControlType type;

    private int eventIndex;
    private AbstractGenericEvent event;

	public ControlEvent(ControlType type) {
		this.type = type;
	}

    public ControlEvent(ControlType type, AbstractGenericEvent event, int eventIndex) {
        this.type = type;
        this.event = event;
        this.eventIndex = eventIndex;
    }

    public ControlType getControlType() {
        return type;
    }

    public AbstractGenericEvent getEvent() {
        return event;
    }

    public int getEventIndex() {
        return eventIndex;
    }

    public static void fire(HasHandlers source, ControlType type) {
		source.fireEvent(new ControlEvent(type));
	}

	public static Type<ControlEventHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<ControlEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return "ControlEvent{type: " + type + "}";
	}

	@Override
	protected void dispatch(ControlEventHandler handler) {
		handler.onControl(this);
	}

	public interface HasControlEventHandlers extends HasHandlers {
		HandlerRegistration addControlEventHandler(ControlEventHandler handler);
	}

	public interface ControlEventHandler extends EventHandler {
		void onControl(ControlEvent event);
	}
}