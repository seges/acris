package sk.seges.acris.theme.pap.accessor;

import javax.lang.model.element.Element;

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

import com.google.gwt.user.client.ui.Widget;

public class ThemeSupportAccessor extends AnnotationAccessor {

	private final ThemeSupport themeSupport;
	private final String name;

	public ThemeSupportAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.themeSupport = this.getAnnotation(element, ThemeSupport.class);
		this.name = element.getSimpleName().toString();
	}

	@Override
	public boolean isValid() {
		return themeSupport != null;
	}

	public Class<? extends Widget> getWidgetClass() {
		return themeSupport.widgetClass();
	}

	public String getElementName() {
		return themeSupport.elementName();
	}

	public String getUiTemplateName() {
		String uiTemplateName = themeSupport.template().value();
		
		if (uiTemplateName != null && !uiTemplateName.equals(Constants.NULL)) {
			return uiTemplateName;
		}

		return name + ".ui.xml";
	}
}