package sk.seges.acris.theme.client.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Target;

import sk.seges.sesam.core.pap.Constants;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

@Target(TYPE)
//@Retention(RetentionPolicy.SOURCE)
public @interface ThemeSupport {

	UiTemplate template() default @UiTemplate(Constants.NULL);

	UiField field() default @UiField;
	
	Class<? extends Widget> widgetClass();
	
	String elementName();
	
	String themeName();
	
	Resource[] resources() default {};
	
	public @interface Resource {

		UiField field() default @UiField;

		String name() default "resources";
		
		Class<? extends ClientBundle> resourceClass();
	}
}