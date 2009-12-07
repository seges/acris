package sk.seges.acris.rebind;

import java.io.PrintWriter;

import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class BeansBindingInitGenerator extends Generator {

	/**
	 * The {@code TreeLogger} used to log messages.
	 */
	private TreeLogger logger = null;

	/**
	 * The generator context.
	 */
	private GeneratorContext context = null;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		this.logger = logger;
		this.context = context;

		JClassType type = null;
		try {
			type = context.getTypeOracle().getType(
					IDomainObject.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find class", e);
			throw new UnableToCompleteException();
		}
		JClassType[] types = type.getSubtypes();

		try {
			logger.log(Type.INFO, "Enable " + typeName + " for introspection");
			return doGenerate(typeName, types);
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, null, e);
			throw new UnableToCompleteException();
		}
	}

	public String doGenerate(String typeName, JClassType[] types)
			throws NotFoundException {
		TypeOracle typeOracle = context.getTypeOracle();
		JClassType type = typeOracle.getType(typeName);
		String packageName = type.getPackage().getName();
		String simpleClassName = type.getSimpleSourceName();
		String className = simpleClassName + "_BeanCreator";
		String qualifiedBeanClassName = packageName + "." + className;
		SourceWriter sourceWriter = getSourceWriter(packageName, className);
		if (sourceWriter == null) {
			return qualifiedBeanClassName;
		}
		
		for (JClassType classType : types) {
			generateCreator(sourceWriter, classType);
		}
		
		sourceWriter.commit(logger);
		
		return qualifiedBeanClassName;
	}

	protected void generateCreator(SourceWriter sourceWriter, JClassType classType) {
		sourceWriter.println(GWT.class.getSimpleName() + ".create(" + classType.getSimpleSourceName() + ".class);");
	}
	
	protected SourceWriter getSourceWriter(String packageName,
			String beanClassName) {
		PrintWriter printWriter = context.tryCreate(logger, packageName,
				beanClassName);

		if (printWriter == null) {
			return null;
		}

		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
				packageName, beanClassName);

		composerFactory.addImport(IDomainObject.class.getCanonicalName());
		composerFactory.addImport(GWT.class.getCanonicalName());

		return composerFactory.createSourceWriter(context, printWriter);
	}
}
