package sk.seges.acris.theme.client.annotation;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.uibinder.client.UiField;

public @interface ThemeResources {

	ThemeResource[] value() default {};
	
	public @interface ThemeResource {

		UiField field() default @UiField;

		String name() default "resources";
		
		Class<? extends ClientBundle> resourceClass();
	}

}
