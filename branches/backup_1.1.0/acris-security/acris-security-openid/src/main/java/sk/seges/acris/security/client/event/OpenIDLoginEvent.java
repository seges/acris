package sk.seges.acris.security.client.event;

import sk.seges.acris.security.client.handler.OpenIDLoginHandler;

import com.google.gwt.event.shared.GwtEvent;

public class OpenIDLoginEvent extends GwtEvent<OpenIDLoginHandler> {

	private static Type<OpenIDLoginHandler> TYPE = new Type<OpenIDLoginHandler>();

	private String identifier;

	public OpenIDLoginEvent() {
	}

	public OpenIDLoginEvent(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public static Type<OpenIDLoginHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<OpenIDLoginHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OpenIDLoginHandler handler) {
		handler.onSubmit(this);
	}
}
