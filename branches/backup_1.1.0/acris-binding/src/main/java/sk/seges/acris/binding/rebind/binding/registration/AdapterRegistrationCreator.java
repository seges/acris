package sk.seges.acris.binding.rebind.binding.registration;

import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;

import sk.seges.acris.binding.client.init.AdaptersRegistration;
import sk.seges.acris.binding.client.providers.support.generic.IBindingBeanAdapterProvider;
import sk.seges.acris.binding.rebind.AbstractCreator;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JConstructor;
import com.google.gwt.user.rebind.SourceWriter;

public class AdapterRegistrationCreator extends AbstractCreator {

	protected final static String IMPL_SUFFIX = "Impl";

	protected Class<?> getAdapterProviderBaseClass() {
		return IBindingBeanAdapterProvider.class;
	}

	@Override
	protected void doGenerate(SourceWriter sourceWriter) throws UnableToCompleteException {
		JClassType adapterProviderClassType = typeOracle.findType(getAdapterProviderBaseClass().getCanonicalName());
		JClassType[] adapterProviders = adapterProviderClassType.getSubtypes();

		sourceWriter.println("public void registerAllAdapters() {");
		sourceWriter.indent();

		for (JClassType adapterProviderImplementationClassType : adapterProviders) {
			if (!adapterProviderImplementationClassType.isAbstract()) {
				boolean found = false;
				
				for (JConstructor constructor :adapterProviderImplementationClassType.getConstructors()) {
					if (constructor.getParameters().length == 0) {
						found = true;
					}
				}
				if (found) {
					sourceWriter.println(BeanAdapterFactory.class.getSimpleName() + ".addProvider(new "
							+ adapterProviderImplementationClassType.getQualifiedSourceName() + "());");
				}
			}
		}

		sourceWriter.indent();
		sourceWriter.println("}");
	}

	@Override
	protected String getOutputSimpleName() {
		return classType.getSimpleSourceName() + IMPL_SUFFIX;
	}

	@Override
	protected String[] getImports() throws UnableToCompleteException {
		return new String[] {BeanAdapterFactory.class.getCanonicalName()};
	}

	@Override
	protected String[] getImplementedInterfaces() {
		return new String[] {AdaptersRegistration.class.getCanonicalName()};
	}

	@Override
	protected String getSuperclassName() {
		return null;
	}
}