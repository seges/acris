package sk.seges.acris.samples;

import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.core.client.util.GWTBeansBinding;

import sk.seges.acris.bind.providers.CheckBoxAdapterProvider;
import sk.seges.acris.bind.providers.ListBoxAutoAdapterProvider;
import sk.seges.acris.bind.providers.TextBoxBaseAdapterProvider;
import sk.seges.acris.samples.form.SimpleForm;
import sk.seges.acris.samples.mocks.Company;
import sk.seges.acris.samples.mocks.SimpleBean;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class FormSimpleBinding implements EntryPoint {

	@Override
	public void onModuleLoad() {

		GWTBeansBinding.init();

		BeanAdapterFactory.addProvider(new TextBoxBaseAdapterProvider());
		BeanAdapterFactory.addProvider(new ListBoxAutoAdapterProvider());
		BeanAdapterFactory.addProvider(new CheckBoxAdapterProvider());

		SimpleForm simpleForm = GWT.create(SimpleForm.class);
		RootPanel.get().add(simpleForm);
		SimpleBean simpleBean = new SimpleBean();
		simpleBean.setName("fat");
		simpleBean.setEmail("office@seges.sk");
		
		Company company = new Company();
		company.setName("Seges s.r.o.");
		simpleBean.setCompany(company);
		
		simpleForm.setBean(simpleBean);
	}
}
