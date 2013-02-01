package sk.seges.acris.security.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public abstract class BasePresenter<D extends BaseDisplay> extends SimpleEventBus {

	protected D display;

	protected List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

	public BasePresenter(D display) {
		this.display = display;
	}

	/**
	 * Binds (adds) the display to the parent widget.
	 * 
	 * @param parent
	 */
	public void bind(HasWidgets parent) {
		final Widget widget = display.asWidget();
		if (widget instanceof DialogBox) {
			((DialogBox) widget).center();
		} else {
			parent.add(display.asWidget());
		}
	}

	/**
	 * Performs cleanup operations, call this after you no longer need to use
	 * the presenter or display.
	 */
	public void unbind() {
		for (HandlerRegistration registration : handlerRegistrations) {
			if (registration != null) {
				registration.removeHandler();
			}
		}
		handlerRegistrations.clear();
	}

	/**
	 * Any handler you register here will be unregistered when unbinding the
	 * presenter.
	 * 
	 * @param handlerRegistration
	 */
	protected void registerHandler(HandlerRegistration handlerRegistration) {
		handlerRegistrations.add(handlerRegistration);
	}
}
