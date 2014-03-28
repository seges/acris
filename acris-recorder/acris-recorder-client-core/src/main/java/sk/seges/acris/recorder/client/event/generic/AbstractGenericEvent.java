package sk.seges.acris.recorder.client.event.generic;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import sk.seges.acris.recorder.client.event.IRecordableEvent;

public abstract class AbstractGenericEvent implements IRecordableEvent {

	public static final String TYPE_INT_ATTRIBUTE = "typeInt";

	protected transient Element el = null;

	protected String type;
	protected boolean canBubble = false;
	protected boolean cancelable = false;

	protected int deltaTime = -1;
	
	protected AbstractGenericEvent() {}

	protected AbstractGenericEvent(Event event) {
		type = DOM.eventGetTypeString(event);
	}
	
	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (canBubble ? 1231 : 1237);
		result = prime * result + (cancelable ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractGenericEvent))
			return false;
		AbstractGenericEvent other = (AbstractGenericEvent) obj;
		if (canBubble != other.canBubble)
			return false;
		if (cancelable != other.cancelable)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

//	public void skipEvent() {
//		if (this.playListList != null) {
//
//			SelectElement selectElement = this.playListList.getElement()
//					.<SelectElement> cast();
//			OptionElement optionElement = selectElement.getOptions().getItem(
//					index);
//			optionElement.setText("-" + optionElement.getText());
//		}
//	}

	public void fireEvent() {

		prepareEvent();

		NativeEvent event = createEvent(el);
		//event
		el.dispatchEvent(event);

		//DomEvent.fireNativeEvent(event, RootPanel.get(), el);
	}

	public void prepareEvent() {


		if (el == null) {
			el = Document.get().getDocumentElement().cast();
		}
	}

	public boolean hasTarget() {
		return false;
	}
	
	protected abstract NativeEvent createEvent(Element el);
	public abstract String toString(boolean pretty, boolean detailed);
	
	public abstract int getTypeInt();
	public abstract void setTypeInt(int type);

	public int getDeltaTime() {
		return deltaTime;
	}

	public void setDeltaTime(int deltaTime) {
		this.deltaTime = deltaTime;
	}
}