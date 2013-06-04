package sk.seges.acris.mvp.client.view.extgwt.user;

import sk.seges.acris.mvp.client.event.LoginEvent;
import sk.seges.acris.mvp.client.event.LoginEvent.LoginEventHandler;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter.LoginDisplay;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class LoginExtGwtView extends ViewImpl implements LoginDisplay {

	private Button button = new Button();

	@Override
	public Widget asWidget() {
		return button;
	}

	public class ButtonEventDelegate extends AbstractEventDelegate<ButtonEvent> {

		private Button button;
		
		public ButtonEventDelegate(Button button) {
			this.button = button;
		}

		@Override
		protected void registerListener(SelectionListener<ButtonEvent> listener) {
			button.addSelectionListener(listener);
		}

		@Override
		protected void removeListener(SelectionListener<ButtonEvent> listener) {
			button.removeListener(Events.Select, listener);
		}
	}
	
	public abstract class AbstractEventDelegate<T extends ComponentEvent> {

		protected SimpleEventBus simpleEventBus = new SimpleEventBus();

		protected AbstractEventDelegate() {
		}
		
		public <H extends EventHandler>  HandlerRegistration delegate(Type<H> type, H handler) {
			HandlerRegistration handlerRegistration = simpleEventBus.addHandler(type, handler);
			final SelectionListener<T> selectionListener = new SelectionListener<T>() {

				@Override
				public void componentSelected(T ce) {
					simpleEventBus.fireEvent(new LoginEvent(null));
				}
			};
			registerListener(selectionListener);
			return new HandlerRegistrationWrapper(handlerRegistration) {
				@Override
				public void onRemoveHandler() {
					removeListener(selectionListener);
				}
			};
		}
		
		abstract protected void registerListener(SelectionListener<T> listener);
		abstract protected void removeListener(SelectionListener<T> listener);
	}
	
	public class HandlerRegistrationWrapper implements HandlerRegistration {

		private HandlerRegistration handlerRegistration;
		
		public HandlerRegistrationWrapper(HandlerRegistration handlerRegistration) {
			this.handlerRegistration = handlerRegistration;
		}
		
		@Override
		public void removeHandler() {
			handlerRegistration.removeHandler();
			onRemoveHandler();
		}
		
		public void onRemoveHandler() {}
	}
	
	@Override
	public HandlerRegistration addLoginHandler(LoginEventHandler handler) {
		return new ButtonEventDelegate(button).delegate(LoginEvent.getType(), handler);
	}

}
