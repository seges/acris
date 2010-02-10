package sk.seges.acris.binding.client.init;

import org.gwt.beansbinding.core.client.util.GWTBeansBinding;

import com.google.gwt.core.client.GWT;

public class BeansBindingInit {
	
	public BeansBindingInit() {
	}

	public static void init() {
		
	    if (GWT.isScript()) {
			try {
				GWT.create(BeansBindingInit.class);
			} catch (Throwable t) {
				GWT.log(t.getMessage(), t);
			}
	    }		

	    GWTBeansBinding.init();
	}
}
