package sk.seges.acris.theme.client.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Target;

import sk.seges.sesam.core.pap.Constants;

import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

@Target(TYPE)
//@Retention(RetentionPolicy.SOURCE)
public @interface ThemeSupport {
	
	Class<? extends Widget> widgetClass();

	String elementName();	

	UiTemplate template() default @UiTemplate(Constants.NULL);
}