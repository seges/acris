package sk.seges.acris.recorder.rpc.event.generic;

import sk.seges.acris.recorder.rpc.bean.IAccessibleBean;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

public abstract class AbstractGenericTargetableEventWithFlags extends
		AbstractGenericTargetableEvent implements IAccessibleBean {

	public static final String CTRL_KEY_INT_ATTRIBUTE = "ctrlKeyInt";
	public static final String ALT_KEY_INT_ATTRIBUTE = "altKeyInt";
	public static final String SHIFT_KEY_INT_ATTRIBUTE = "shiftKeyInt";
	public static final String META_KEY_INT_ATTRIBUTE = "metaKeyInt";
	
	protected boolean ctrlKey;
	protected boolean altKey;
	protected boolean shiftKey;
	protected boolean metaKey;

	public AbstractGenericTargetableEventWithFlags() {
	}

	public int getMetaKeyInt() {
		return metaKey ? 1 : 0;
	}

	public int getShiftKeyInt() {
		return shiftKey ? 1 : 0;
	}

	public int getAltKeyInt() {
		return altKey ? 1 : 0;
	}

	public int getCtrlKeyInt() {
		return ctrlKey ? 1 : 0;
	}

	public void setCtrlKeyInt(int ctrlKey) {
		this.ctrlKey = ctrlKey == 1;
	}

	public void setAltKeyInt(int altKey) {
		this.altKey = altKey == 1;
	}

	public void setShiftKeyInt(int shiftKey) {
		this.shiftKey = shiftKey == 1;
	}

	public void setMetaKeyInt(int metaKey) {
		this.metaKey = metaKey == 1;
	}

	public AbstractGenericTargetableEventWithFlags(Event event) {
		super(event);

		altKey = DOM.eventGetAltKey(event);
		ctrlKey = DOM.eventGetCtrlKey(event);
		metaKey = DOM.eventGetMetaKey(event);
		shiftKey = DOM.eventGetShiftKey(event);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (altKey ? 1231 : 1237);
		result = prime * result + (ctrlKey ? 1231 : 1237);
		result = prime * result + (metaKey ? 1231 : 1237);
		result = prime * result + (shiftKey ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof AbstractGenericTargetableEventWithFlags))
			return false;
		AbstractGenericTargetableEventWithFlags other = (AbstractGenericTargetableEventWithFlags) obj;
		if (altKey != other.altKey)
			return false;
		if (ctrlKey != other.ctrlKey)
			return false;
		if (metaKey != other.metaKey)
			return false;
		if (shiftKey != other.shiftKey)
			return false;
		return true;
	}
}
