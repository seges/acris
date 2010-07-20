package sk.seges.acris.core.showcase.client;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.core.client.component.semaphore.Semaphore;
import sk.seges.acris.core.showcase.shared.OrderService;
import sk.seges.acris.core.showcase.shared.OrderServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author ladislav.gazo
 */
public class Site implements EntryPoint {

	public void onModuleLoad() {
		RootPanel root = RootPanel.get();
		final FlowPanel container = new FlowPanel();
		root.add(container);

		final OrderServiceAsync customerService = GWT.create(OrderService.class);
		// ServiceDefTarget endpoint = (ServiceDefTarget) customerService;
		// endpoint.setServiceEntryPoint("showcase-service/orderService");

		final List<Call> calls = new ArrayList<Call>();
		final Semaphore semaphore = new Semaphore(2);
		
		OrderPanel order = new OrderPanel(calls);
		final StatusPanel status = new StatusPanel(semaphore);
		
		container.add(order);
		container.add(status);
		container.add(new Button("Order", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				status.raise(calls.size());
				
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						semaphore.signal(0);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						semaphore.signal(1);
					}
				};
				
				for(Call call : calls) {
					customerService.order(call.getOrderName(), call.getPrice(), callback);
				}
				
				calls.clear();
			}
		}));
	}
}
