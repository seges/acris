package sk.seges.acris.showcase.client.view.core.adapter.smartgwt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.smartgwt.client.core.JsObject;
import com.smartgwt.client.widgets.Canvas;

public class HasClickHandlerSmartProvider extends Composite implements HasClickHandlers {

	private com.smartgwt.client.widgets.form.fields.events.HasClickHandlers ownerForm;
	private com.smartgwt.client.widgets.events.HasClickHandlers ownerCanvas;

	public HasClickHandlerSmartProvider(com.smartgwt.client.widgets.form.fields.events.HasClickHandlers handlerOwner) {
		this.ownerForm = handlerOwner;

		handlerOwner.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				ClickEvent.fireNativeEvent(createEvent(event), HasClickHandlerSmartProvider.this);
			}
		});
	}

	public HasClickHandlerSmartProvider(com.smartgwt.client.widgets.events.HasClickHandlers handlerOwner) {
		this.ownerCanvas = handlerOwner;

		handlerOwner.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				ClickEvent.fireNativeEvent(createEvent(event), HasClickHandlerSmartProvider.this);
			}
		});
	}

	protected NativeEvent createEvent(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
		Element element = ((JsObject) ownerForm).getJsObj().cast();
		return createEvent(element);
	}

	protected NativeEvent createEvent(com.smartgwt.client.widgets.events.ClickEvent event) {
		Element element = ((Canvas) ownerCanvas).getJsObj().cast();
		return createEvent(element);
	}

	protected NativeEvent createEvent(Element el) {
		return Document.get().createMouseEvent(ClickEvent.getType().getName(), true, true, 0, 0, 0, 0, 0, false, false, false, false, 0, el);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
}
