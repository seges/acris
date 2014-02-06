package sk.seges.acris.security.client.event;

import sk.seges.acris.security.client.handler.CancelLoginHandler;

import com.google.gwt.event.shared.GwtEvent;

public class CancelLoginEvent extends GwtEvent<CancelLoginHandler> {

	private static Type<CancelLoginHandler> TYPE = new Type<CancelLoginHandler>();
	
	public CancelLoginEvent() {
	}
	
	public static Type<CancelLoginHandler> getType() {
		return TYPE;
	}
	
	@Override
	public Type<CancelLoginHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CancelLoginHandler handler) {
		handler.onSubmit(this);
	}

}
