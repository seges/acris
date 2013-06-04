package sk.seges.acris.generator.rebind;

import java.io.PrintWriter;

import sk.seges.acris.generator.client.configuration.GeneratorConfiguration;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class GeneratorConfigurationGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext generatorContext, String typeName)
			throws UnableToCompleteException {

		TypeOracle typeOracle = generatorContext.getTypeOracle();
		JClassType sourceType = typeOracle.findType(typeName);

		String generatedSimpleSourceName = sourceType.getQualifiedSourceName().replace('.', '_').replace('$','_') + "Impl";

		// Begin writing the generated source.
		ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(sourceType.getPackage()
				.getName(), generatedSimpleSourceName);
		String createdClassName = f.getCreatedClassName();

		f.addImport(GeneratorConfiguration.class.getCanonicalName());
		f.addImplementedInterface(GeneratorConfiguration.class.getSimpleName());

		PrintWriter out = generatorContext.tryCreate(logger, sourceType.getPackage().getName(),
				generatedSimpleSourceName);

		// If an implementation already exists, we don't need to do any work
		if (out != null) {
			SourceWriter sw = f.createSourceWriter(generatorContext, out);

			sw.println("public String getWebId() { return \"" + System.getProperty("webId") + "\"; }");
			sw.println("public String getProperties() { return \"" + System.getProperty("properties") + "\"; }");
			sw.println("public String getLanguage() { return \"" + System.getProperty("language") + "\"; }");
		
			sw.commit(logger);
		}

		return createdClassName;
	}
}
