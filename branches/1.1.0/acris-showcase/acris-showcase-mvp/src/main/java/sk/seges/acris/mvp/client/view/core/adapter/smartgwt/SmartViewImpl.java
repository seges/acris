package sk.seges.acris.mvp.client.view.core.adapter.smartgwt;

import java.util.Set;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.validation.client.InvalidConstraint;
import com.philbeaudoin.gwtp.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.form.fields.FormItem;

public abstract class SmartViewImpl extends ViewImpl {

	private HandlerManager handlerManager;

	HandlerManager ensureHandlers() {
		return handlerManager == null ? handlerManager = new HandlerManager(this) : handlerManager;
	}

	HandlerManager getHandlerManager() {
		return handlerManager;
	}

	protected HasClickHandlers gwit(com.smartgwt.client.widgets.form.fields.events.HasClickHandlers handlerOwner) {
		return new HasClickHandlerSmartProvider(handlerOwner);
	}

	protected <T> HasValue<T> gwit(FormItem formItem) {
		return new HasValueSmartProvider<T>(formItem);
	}

	protected final <H extends EventHandler> HandlerRegistration addHandler(final H handler, GwtEvent.Type<H> type) {
		return ensureHandlers().addHandler(type, handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		if (handlerManager != null) {
			handlerManager.fireEvent(event);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <Z> Set<InvalidConstraint<Z>> castSet(Set<?> set, Class<Z> type) {
		return (Set<InvalidConstraint<Z>>) set;
	}
}