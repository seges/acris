package sk.seges.acris.binding.rebind.loader;

import java.util.List;

import sk.seges.acris.binding.rebind.GeneratorException;
import sk.seges.sesam.dao.IAsyncDataLoader;
import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.user.rebind.SourceWriter;

public class FieldSpecLoaderCreator implements ILoaderCreator {

	private Class<? extends IAsyncDataLoader<List<? extends IDomainObject<?>>>> dataLoaderClass;
	
	public void setDataLoaderCreatorClass(Class<?> dataLoaderClass) {
		this.dataLoaderClass = (Class<? extends IAsyncDataLoader<List<? extends IDomainObject<?>>>>)dataLoaderClass;
	}
	
	private String getFirstLowerCase(String text) {
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}

	private String getFirstUpperCase(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

	@Override
	public String generateLoader(SourceWriter sourceWriter,
			JClassType classType, String propertyReference,
			String targetListWidgetName, String beanProxyWrapper, String binding, JField field)
			throws GeneratorException {
		
		if (dataLoaderClass == null) {
			throw new GeneratorException("No data loader class is defined. Please define data loader class before you call generateLoader.");
		}

		String lowerClassName = getFirstLowerCase(classType.getSimpleSourceName());
		String loaderName = lowerClassName + "DataLoader";
		
		sourceWriter.println(dataLoaderClass.getCanonicalName() + " " + loaderName + " = new " + dataLoaderClass.getCanonicalName() + "();");
		
//		sourceWriter.println(loaderName + ".load(" + Page.class.getSimpleName() + ".ALL_RESULTS_PAGE, new " + ICallback.class.getSimpleName() + "<" + PagedResult.class.getSimpleName() + "<" + List.class.getSimpleName() + "<" + classType.getSimpleSourceName() + ">>>() {");
//		sourceWriter.indent();
//		sourceWriter.println("@Override");
//		sourceWriter.println("public void onSuccess(" + PagedResult.class.getSimpleName() + "<" + List.class.getSimpleName() + "<" + classType.getSimpleSourceName() + ">> resultList) {");
//		sourceWriter.indent();
//		sourceWriter.println("for (" + classType.getSimpleSourceName() + " " + classType.getSimpleSourceName().toLowerCase() + " : resultList.getResult()) {");
//		sourceWriter.indent();
//		sourceWriter.println(targetListWidgetName + ".addItem(" + classType.getSimpleSourceName().toLowerCase() + ".get" + getFirstUpperCase(propertyReference) + "());");
//		sourceWriter.outdent();
//		sourceWriter.println("}");
//		sourceWriter.println(getFirstLowerCase(beanProxyWrapper) + ".setProxyValues(resultList.getResult());");
//		sourceWriter.println(binding + ".bind();");
//		sourceWriter.outdent();
//		sourceWriter.println("}");
//		sourceWriter.println("");
//		sourceWriter.println("@Override");
//		sourceWriter.println("public void onFailure(Throwable ex) {");
//		sourceWriter.indent();
//		sourceWriter.println(GWT.class.getSimpleName() + ".log(\"Exception occured\",ex);");
//		sourceWriter.println("}");
//		sourceWriter.println("");
//		sourceWriter.outdent();
//		sourceWriter.println("});");
		
		return loaderName;
	}

	@Override
	public String[] getImports() {
		return new String[] {
				IAsyncDataLoader.class.getCanonicalName(),
				IDomainObject.class.getCanonicalName(),
				Page.class.getCanonicalName(),
				ICallback.class.getCanonicalName(),
				PagedResult.class.getCanonicalName(),
				List.class.getCanonicalName(),
				GWT.class.getCanonicalName()
		};
	}
}