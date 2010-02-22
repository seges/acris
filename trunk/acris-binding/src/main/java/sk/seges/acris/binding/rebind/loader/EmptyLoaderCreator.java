package sk.seges.acris.binding.rebind.loader;

import sk.seges.acris.binding.rebind.GeneratorException;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.user.rebind.SourceWriter;

public class EmptyLoaderCreator implements ILoaderCreator {

	@Override
	public String generateLoader(SourceWriter sourceWriter,
			JClassType classType, String propertyReference,
			String targetWidgetName, String beanProxyWrapper, String binding, JField field)
			throws GeneratorException {
		return null;
	}

	@Override
	public String[] getImports() {
		return new String[0];
	}
}