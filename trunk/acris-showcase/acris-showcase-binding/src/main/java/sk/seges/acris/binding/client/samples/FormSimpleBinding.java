package sk.seges.acris.binding.client.samples;

import java.util.Date;
import java.util.Set;

import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.core.client.util.GWTBeansBinding;
import org.gwt.beansbinding.ui.client.adapters.HasHTMLAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.HasTextAdapterProvider;

import sk.seges.acris.binding.client.holder.validation.ValidationMediator;
import sk.seges.acris.binding.client.init.AdaptersRegistration;
import sk.seges.acris.binding.client.init.BeansBindingInit;
import sk.seges.acris.binding.client.providers.ListBoxAdapterProvider;
import sk.seges.acris.binding.client.providers.wrapper.BeanWrapperAdapterProvider;
import sk.seges.acris.binding.client.samples.form.ExampleHighlighter;
import sk.seges.acris.binding.client.samples.form.SimpleForm;
import sk.seges.acris.binding.client.samples.mocks.Company;
import sk.seges.acris.binding.client.samples.mocks.ContactCheck;
import sk.seges.acris.binding.client.samples.mocks.SimpleBean;
import sk.seges.acris.binding.client.samples.mocks.SimpleBeanBeanWrapper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.validation.client.InvalidConstraint;
import com.google.gwt.validation.client.interfaces.IValidator;

public class FormSimpleBinding implements EntryPoint {

	private Button selectedButton;
	private VerticalPanel errorsPanel;
	
	@Override
	public void onModuleLoad() {

		BeansBindingInit.init();
		GWTBeansBinding.init();
		
		BeanAdapterFactory.addProvider(new HasTextAdapterProvider());
		BeanAdapterFactory.addProvider(new HasHTMLAdapterProvider());
		HasHTMLAdapterProvider.register(HTML.class);
		
		BeanAdapterFactory.addProvider(new ListBoxAdapterProvider());
		AdaptersRegistration registration = GWT.create(AdaptersRegistration.class);
		registration.registerAllAdapters();

		BeanAdapterFactory.addProvider(new BeanWrapperAdapterProvider());

		errorsPanel = new VerticalPanel();
		ExampleHighlighter.errorsPanel = errorsPanel;

		ResizePanel rp = new ResizePanel();
		rp.add(new HTML("ADAD"));
		RootPanel.get().add(rp);
		
		final Button button1 = GWT.create(Button.class);
		button1.setText("jozef");
		RootPanel.get().add(button1);

		final Button button2 = GWT.create(Button.class);
		button2.setText("andre");
		RootPanel.get().add(button2);

		final Button button3 = GWT.create(Button.class);
		button3.setText("svatozar");
		RootPanel.get().add(button3);

		final Button button4 = GWT.create(Button.class);
		button4.setText("urpin");
		RootPanel.get().add(button4);
		
		RootPanel.get().add(new Label("Errors:"));
		RootPanel.get().add(errorsPanel);
		
		final SimpleForm simpleForm = GWT.create(SimpleForm.class);
		RootPanel.get().add(simpleForm);
		final Button submit = GWT.create(Button.class);
		submit.setText("Submit");
		RootPanel.get().add(submit);

		submit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(selectedButton != null) {
					selectedButton.setText(simpleForm.getBean().getName());
				}
				
				IValidator<SimpleBean> validator = GWT.create(SimpleBeanBeanWrapper.class);
				Set<InvalidConstraint<SimpleBean>> constraints = validator.validate(simpleForm.getBean());
				ValidationMediator.highlightConstraints(simpleForm, constraints);
			}
		});
		
		final Button submitExtended = GWT.create(Button.class);
		submitExtended.setText("Submit extended");
		RootPanel.get().add(submitExtended);

		submitExtended.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(selectedButton != null) {
					selectedButton.setText(simpleForm.getBean().getName());
				}
				
				IValidator<SimpleBean> validator = GWT.create(SimpleBeanBeanWrapper.class);
				
				String[] groups = new String[] { ContactCheck.class.getName() };
				Set<InvalidConstraint<SimpleBean>> constraints = validator.validate(simpleForm.getBean(), groups);
				ValidationMediator.highlightConstraints(simpleForm, constraints);
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
		
		simpleForm.setBean(new SimpleBean());
	}
	
	private SimpleBean createBean(String name, String mail, String companyName) {
		SimpleBean fat = new SimpleBean();
		fat.setName(name);
		fat.setEmail(mail);
		fat.setDate(new Date());
		
		Company company = new Company();
		company.setName(companyName);
		fat.setCompany(company);
		
		return fat;
	}
	
	private SimpleBean bean1;
	
	private SimpleBean createBean1() {
		if (bean1 != null) {
			return bean1;
		}
		
		bean1 = createBean("jozef", "jozef [at] seges [dot] sk", "Seges s.r.o.");		
		return bean1;
	}

	private SimpleBean bean2;
	
	private SimpleBean createBean2() {
		if (bean2 != null) {
			return bean2;
		}
	
		bean2 = createBean("andre", null, "Zettaflops s.r.o");
				
		return bean2;
	}

	private SimpleBean bean3;

	private SimpleBean createBean3() {
		if (bean3 != null) {
			return bean3;
		}
		bean3 = createBean("svatozar", "svatozar [at] seges [dot] sk", "Seges s.r.o.");
		
		return bean3;
	}

	private SimpleBean bean4;
	
	private SimpleBean createBean4() {
		if (bean4 != null) {
			return bean4;
		}
		
		bean4 = createBean("urpin", "urpin [at] seges [dot] sk", "Seges s.r.o.");
		
		return bean4;
	}
}