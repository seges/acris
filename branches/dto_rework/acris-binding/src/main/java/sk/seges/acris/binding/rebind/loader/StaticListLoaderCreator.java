/**
 * 
 */
package sk.seges.acris.binding.rebind.loader;

import sk.seges.acris.binding.client.loader.StaticListLoader;
import sk.seges.acris.binding.rebind.GeneratorException;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Loader creator responsible for creating a loader providing its values from a
 * static list of values. It is suitable e.g. for enums.
 * 
 * @author eldzi
 */
public class StaticListLoaderCreator implements ILoaderCreator {
	private String staticList;

	public void setStaticList(String staticList) {
		this.staticList = staticList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sk.seges.acris.binding.rebind.loader.ILoaderCreator#generateLoader(com
	 * .google.gwt.user.rebind.SourceWriter,
	 * com.google.gwt.core.ext.typeinfo.JClassType, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String generateLoader(SourceWriter sourceWriter, JClassType classType, String propertyReference,
			String targetWidgetName, String beanProxyWrapper, String binding, JField field)
			throws GeneratorException {
		String loaderName = field.getName() + "DataLoader";

		String loaderClass = StaticListLoader.class.getCanonicalName() + "<"
				+ classType.getQualifiedSourceName() + ">";
		sourceWriter.println(loaderClass + " " + loaderName + " = new " + loaderClass + "(" + staticList
				+ ");");

		return loaderName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sk.seges.acris.binding.rebind.loader.ILoaderCreator#getImports()
	 */
	@Override
	public String[] getImports() {
		return null;
	}
}
