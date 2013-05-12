package sk.seges.acris.theme.rebind;

import sk.seges.acris.theme.client.annotation.Theme;
import sk.seges.acris.theme.client.annotation.ThemeSupport;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.SelectionProperty;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;

public class ThemeComponentSelectorGenerator extends Generator {

	public static final String SUFFIX = "Panel";

	@Override
	public String generate(TreeLogger logger, GeneratorContext generatorContext, String typeName) throws UnableToCompleteException {

		SelectionProperty selectionProperty;
		try {
			selectionProperty = generatorContext.getPropertyOracle().getSelectionProperty(logger, "acristheme");
		} catch (BadPropertyValueException e) {
			logger.log(Type.INFO, "Acris theme property is not defined");
			return typeName;
		}

		if (selectionProperty == null) {
			return typeName;
		}
		
		if (selectionProperty.getCurrentValue() == null) {
			return typeName;
		}
		
		String themeName = selectionProperty.getCurrentValue();
		
		for (JType type: generatorContext.getTypeOracle().getTypes()) {
			if (type.isClassOrInterface() != null) {
				JClassType classType = type.isClassOrInterface();
				Theme theme = classType.getAnnotation(Theme.class);
				ThemeSupport themeSupport = classType.getAnnotation(ThemeSupport.class);
				if (theme != null) {
					if (themeName.equals(theme.value()) && themeSupport.widgetClass().getCanonicalName().equals(typeName)) {
						return classType.getPackage().getName() + "." + classType.getSimpleSourceName() + SUFFIX;
					}
				}
			}
		}
		
		return typeName;
	}
}