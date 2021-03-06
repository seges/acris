package sk.seges.acris.generator.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import sk.seges.acris.generator.client.configuration.GeneratorConfiguration;

import java.io.PrintWriter;

public class GeneratorConfigurationGenerator extends Generator {

	private static final String WEB_ID_NAME = "webId";
	private static final String PROPERTIES_NAME = "properties";
	private static final String LANGUAGE_NAME = "language";
	private static final String ALIAS_NAME = "alias";
	private static final String CONTENT_START_INDEX_NAME = "contentStartIndex";
	private static final String CONTENT_PAGE_SIZE_NAME = "contentPageSize";

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
			
			sw.println("public String getWebId() { return " + getStringProperty(WEB_ID_NAME) + "; }");
			sw.println("public String getProperties() { return " + getStringProperty(PROPERTIES_NAME) + "; }");
			sw.println("public String getLanguage() { return " + getStringProperty(LANGUAGE_NAME) + "; }");
			sw.println("public String getAlias() { return " + getStringProperty(ALIAS_NAME) + "; }");

			sw.println("public int getContentStartIndex() { return " + getIntegerProperty(CONTENT_START_INDEX_NAME, 0) + "; }");
			sw.println("public int getContentPageSize() { return " + getIntegerProperty(CONTENT_PAGE_SIZE_NAME, -1) + "; }");
		
			sw.commit(logger);
		}

		return createdClassName;
	}
		
	private String getStringProperty(String name) {
		String value = System.getProperty(name);
		if (value == null) {
			return null;
		}
		return "\"" + value + "\"";
	}

	private String getIntegerProperty(String name, int defaultValue) {
		String value = System.getProperty(name);
		if (value == null) {
			return "" + defaultValue;
		}
		return value;
	}
}