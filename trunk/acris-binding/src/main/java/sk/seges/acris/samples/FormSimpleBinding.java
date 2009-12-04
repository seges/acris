package sk.seges.acris.samples;

import sk.seges.acris.samples.form.SimpleForm;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class FormSimpleBinding implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new SimpleForm());
	}
}
