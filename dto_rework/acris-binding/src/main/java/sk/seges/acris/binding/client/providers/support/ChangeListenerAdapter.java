/**
 * 
 */
package sk.seges.acris.binding.client.providers.support;

import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

public class ChangeListenerAdapter<M extends SourcesChangeEvents, T> extends BeanAdapterBase {
	
	private M widget;		
	private Handler handler;
	private T previousValue;
	private AbstractBindingChangeListenerAdapterProvider<M, T> provider;
	
	public ChangeListenerAdapter(M changeHandlerWidget, String property, AbstractBindingChangeListenerAdapterProvider<M, T> provider) {
		super(property);
		this.widget = changeHandlerWidget;
		this.provider = provider;
	}

	public T getValue() {
		return provider.getValue(widget);
	}

	public void setValue(T t) {
		provider.setValue(widget, t);
	}

	@Override
	protected void listeningStarted() {
		handler = new Handler();
		previousValue = getValue();
		widget.addChangeListener(handler);
	}

	@Override
	protected void listeningStopped() {
		if (handler != null) {
			widget.removeChangeListener(handler);
			handler = null;
		}
		previousValue = null;
	}

	private class Handler implements ChangeListener {
		@Override
		public void onChange(Widget sender) {
			Object oldElementOrElements = previousValue;
			previousValue = getValue();
			firePropertyChange(oldElementOrElements, previousValue);
		}
	}
}