package sk.seges.acris.binding.client.samples;

import java.util.Date;

import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;

import sk.seges.acris.binding.client.bind.providers.CheckBoxAdapterProvider;
import sk.seges.acris.binding.client.bind.providers.DateBoxAdapterProvider;
import sk.seges.acris.binding.client.bind.providers.ListBoxAutoAdapterProvider;
import sk.seges.acris.binding.client.bind.providers.TextBoxBaseAdapterProvider;
import sk.seges.acris.binding.client.init.BeansBindingInit;
import sk.seges.acris.binding.client.samples.form.SimpleForm;
import sk.seges.acris.binding.client.samples.mocks.Company;
import sk.seges.acris.binding.client.samples.mocks.SimpleBean;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class FormSimpleBinding implements EntryPoint {

	private Button selectedButton;
	
	@Override
	public void onModuleLoad() {

		BeansBindingInit.init();

		BeanAdapterFactory.addProvider(new TextBoxBaseAdapterProvider());
		BeanAdapterFactory.addProvider(new ListBoxAutoAdapterProvider());
		BeanAdapterFactory.addProvider(new CheckBoxAdapterProvider());
		BeanAdapterFactory.addProvider(new DateBoxAdapterProvider());

		final Button button1 = GWT.create(Button.class);
		button1.setText("fat");
		RootPanel.get().add(button1);

		final Button button2 = GWT.create(Button.class);
		button2.setText("alik");
		RootPanel.get().add(button2);

		final Button button3 = GWT.create(Button.class);
		button3.setText("eldzi");
		RootPanel.get().add(button3);

		final Button button4 = GWT.create(Button.class);
		button4.setText("mig");
		RootPanel.get().add(button4);
		
		final SimpleForm simpleForm = GWT.create(SimpleForm.class);
		RootPanel.get().add(simpleForm);
		final Button submit = GWT.create(Button.class);
		submit.setText("Submit");
		RootPanel.get().add(submit);

		submit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectedButton.setText(simpleForm.getBean().getName());
			}
		});
		
		button1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				simpleForm.setBean(createBean1());
				selectedButton = button1;
			}
		});

		button2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				simpleForm.setBean(createBean2());
				selectedButton = button2;
			}
		});

		button3.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				simpleForm.setBean(createBean3());
				selectedButton = button3;
			}
		});

		button4.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				simpleForm.setBean(createBean4());
				selectedButton = button4;
			}
		});
	}
	
	private SimpleBean fat;
	
	private SimpleBean createBean1() {
		if (fat != null) {
			return fat;
		}
		
		fat = new SimpleBean();
		fat.setName("fat");
		fat.setEmail("simun [at] seges [dot] sk");
		fat.setDate(new Date());
		
		Company company = new Company();
		company.setName("Seges s.r.o.");
		fat.setCompany(company);
		
		return fat;
	}

	private SimpleBean alik;
	
	private SimpleBean createBean2() {
		if (alik != null) {
			return alik;
		}
		
		alik = new SimpleBean();
		alik.setName("alik");
		alik.setEmail("alac [at] zfs [dot] sk");
		
		Company company = new Company();
		company.setName("Zettaflops s.r.o");
		alik.setCompany(company);
		
		return alik;
	}

	private SimpleBean eldzi;

	private SimpleBean createBean3() {
		if (eldzi != null) {
			return eldzi;
		}
		eldzi = new SimpleBean();
		eldzi.setName("eldzi");
		eldzi.setEmail("gazo [at] seges [dot] sk");
		
		Company company = new Company();
		company.setName("Seges s.r.o.");
		eldzi.setCompany(company);
		
		return eldzi;
	}

	private SimpleBean mig;
	
	private SimpleBean createBean4() {
		if (mig != null) {
			return mig;
		}
		
		mig = new SimpleBean();
		mig.setName("mig");
		mig.setEmail("grosos [at] seges [dot] sk");
		
		Company company = new Company();
		company.setName("Seges s.r.o.");
		mig.setCompany(company);
		
		return mig;
	}
}