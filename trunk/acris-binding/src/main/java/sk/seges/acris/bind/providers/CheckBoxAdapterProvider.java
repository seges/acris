package sk.seges.acris.bind.providers;

import sk.seges.acris.bind.providers.annotations.OneToOne;
import sk.seges.acris.bind.providers.support.AbstractBindingClickHandlerAdapterProvider;

import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author fat
 */
@OneToOne
public final class CheckBoxAdapterProvider extends AbstractBindingClickHandlerAdapterProvider<CheckBox, Boolean> {

	@Override
	public Class<CheckBox> getBindingWidgetClasses() {
		return CheckBox.class;
	}

	@Override
	protected Boolean getValue(CheckBox widget) {
		return widget.getValue();
	}

	@Override
	protected void setValue(CheckBox widget, Boolean t) {
		if (t != null) {
			widget.setValue(t);
		} else {
			widget.setValue(false);
		}
	}
}