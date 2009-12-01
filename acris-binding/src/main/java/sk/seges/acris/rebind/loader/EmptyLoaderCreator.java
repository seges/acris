package sk.seges.acris.rebind.loader;

import sk.seges.acris.rebind.GeneratorException;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.SourceWriter;

public class EmptyLoaderCreator implements ILoaderCreator {

	@Override
	public boolean generateLoader(SourceWriter sourceWriter,
			JClassType classType, String propertyReference,
			String targetWidgetName, String beanProxyWrapper, String binding)
			throws GeneratorException {
		return true;
	}

	@Override
	public String[] getImports() {
		return new String[0];
	}
}