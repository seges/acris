package sk.seges.acris.binding.rebind.binding;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Set;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.client.annotations.BindingFieldsBase;
import sk.seges.acris.binding.client.annotations.ValidationStrategy;
import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.holder.IBeanBindingHolder;
import sk.seges.acris.binding.client.holder.validation.ValidatableBeanBinding;
import sk.seges.acris.binding.client.holder.validation.ValidatableBindingHolder;
import sk.seges.acris.binding.client.holder.validation.ValidationHighligther;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.binding.rebind.GeneratorException;
import sk.seges.acris.binding.rebind.binding.support.BindingCreatorFactory;
import sk.seges.acris.binding.rebind.binding.support.IBindingCreator;
import sk.seges.acris.binding.rebind.loader.DefaultLoaderCreatorFactory;
import sk.seges.acris.binding.rebind.loader.EmptyLoaderCreator;
import sk.seges.acris.core.rebind.RebindUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.validation.client.InvalidConstraint;

public class BeanBindingCreator {
	public static final String WRAPPER_SUFFIX = "BeanWrapper";
	
	private static final String BINDING_HOLDER_VAR = "_bindingHolder";

	private String bindingHolder = null;
	
	private TypeOracle typeOracle;
	
	private JClassType bindingBeanClassType;
	private String packageName;
	
	private boolean validationEnabled = false;

	protected String getResultSuffix() {
		return "_BindingWrapper";
	}
	
	/**
	 * Set default loader creator. Empty loader is default loader for x-To-Many bindings
	 */
	protected void setDefaultLoaderCreator() {
		DefaultLoaderCreatorFactory.setDefaultLoaderCreator(EmptyLoaderCreator.class);
	}
	
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		setDefaultLoaderCreator();
		
		this.typeOracle = context.getTypeOracle();
		
		assert typeOracle != null;

		JClassType classType = null;
		try {
			classType = typeOracle.getType(typeName);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to find classtype for " + typeName);
			throw new UnableToCompleteException();
		}

		packageName = classType.getPackage().getName();

		try {
			bindingBeanClassType = RebindUtils.getGenericsFromInterfaceType(classType, typeOracle.findType(IBeanBindingHolder.class.getName()), 0);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to extract generic type class from interface");
			throw new UnableToCompleteException();
		}

		String className = classType.getSimpleSourceName() + getResultSuffix();

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, className);

		PrintWriter printWriter = null;
		printWriter = context.tryCreate(logger, packageName, className);
		
		if (printWriter == null) {
			return packageName + "." + className;
		}
		
		try {
			BindingCreatorFactory.fillSupportedTypes(context.getTypeOracle());
		} catch (GeneratorException e) {
			logger.log(Type.ERROR, e.getMessage());
			throw new UnableToCompleteException();
		}
		
		String[] imports = new String[] { 
				UpdateStrategy.class.getCanonicalName(),
				GWT.class.getCanonicalName(),
				BindingHolder.class.getCanonicalName(),
				BeanWrapper.class.getCanonicalName(),
				bindingBeanClassType.getQualifiedSourceName(),
				bindingBeanClassType.getQualifiedSourceName() + WRAPPER_SUFFIX};

		//initialize binding context for all related creators
		BindingCreatorFactory.setBindingContext(bindingBeanClassType, packageName);

		//Adding biding specific imports
		for(JField field : classType.getFields()) {
			BindingField bindingFieldAnnotation = field.getAnnotation(BindingField.class);
			if (bindingFieldAnnotation == null) {
				continue;
			}
			
			IBindingCreator<? extends Annotation> bindingGenerator = BindingCreatorFactory.getBindingCreator(field);
			
			if (bindingGenerator != null) {
				String[] bindingImports = bindingGenerator.getImports(field, bindingFieldAnnotation);
				
				for (String imp : bindingImports) {
					composer.addImport(imp);
				}
			}
		}
		
		for (String imp : imports) {
			composer.addImport(imp);
		}

		composer.setSuperclass(typeName);

		validationEnabled = isValidationEnabled(classType.getAnnotation(BindingFieldsBase.class));
		
		if(validationEnabled) {
			composer.addImplementedInterface(ValidatableBeanBinding.class.getName() + "<" + bindingBeanClassType.getName() + ">");
		}
		
		SourceWriter sourceWriter = null;
		sourceWriter = composer.createSourceWriter(context, printWriter);

		//initialize generator context for all related creators
		BindingCreatorFactory.setGeneratorContext(context, sourceWriter, logger);

		generateConstructor(sourceWriter, classType, className);
		getnerateBindingMethods(sourceWriter, bindingBeanClassType);
		generateValidationBeanBindingMethods(sourceWriter, classType, bindingBeanClassType);
		
		//initialize binding form for all related creators
		BindingCreatorFactory.setBindingHolder(bindingHolder);

		generateFields(sourceWriter, classType);
		
		generateOnLoadMethod(sourceWriter, classType);

		sourceWriter.outdent();
		sourceWriter.println("}");

		context.commit(logger, printWriter);

		return packageName + "." + className;
	}

	private void generateValidationBeanBindingMethods(SourceWriter sw, JClassType classType,
			JClassType bindingBeanClassType) {
		if (!validationEnabled) {
			return;
		}

		sw.println("@Override");
		sw.println("public void highlightConstraints(" + Set.class.getName() + "<"
				+ InvalidConstraint.class.getName() + "<" + bindingBeanClassType.getSimpleSourceName()
				+ ">> constraints) {");
		sw.indent();
		sw.println("((" + ValidatableBindingHolder.class.getName() + ")" + BINDING_HOLDER_VAR
				+ ").highlightConstraints(constraints);");
		sw.outdent();
		sw.println("}");
		sw.println();
		sw.println("@Override");
		sw.println("public void clearHighlight() {");
		sw.indent();
		sw.println("((" + ValidatableBindingHolder.class.getName() + ")" + BINDING_HOLDER_VAR
				+ ").clearHighlight();");
		sw.outdent();
		sw.println("}");
	}

	private boolean isValidationEnabled(BindingFieldsBase bindingFieldsBaseAnnotation) {
		return bindingFieldsBaseAnnotation.validationStrategy() != null && !ValidationStrategy.NEVER.equals(bindingFieldsBaseAnnotation.validationStrategy());
	}

	private boolean getnerateBindingMethods(SourceWriter sourceWriter, JClassType beanClassType) {
		sourceWriter.println("private " + BeanWrapper.class.getSimpleName() + "<" + beanClassType.getSimpleSourceName() + "> _beanWrapper;");
		sourceWriter.println("");
		sourceWriter.println("public void setBean(" + beanClassType.getSimpleSourceName() + " bean) {");
		sourceWriter.indent();
		sourceWriter.println("getBeanWrapper().setContent(bean);");
		sourceWriter.println(bindingHolder + ".setBean(bean);");
		sourceWriter.println("super.setBean(bean);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("public " + beanClassType.getSimpleSourceName() + " getBean() {");
		sourceWriter.indent();
		sourceWriter.println("return getBeanWrapper().getContent();");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("public " + BeanWrapper.class.getSimpleName() + "<" + beanClassType.getSimpleSourceName() + "> getBeanWrapper() {");
		sourceWriter.indent();
		sourceWriter.println("if (_beanWrapper == null) {");
		sourceWriter.indent();
		sourceWriter.println("_beanWrapper = GWT.create(" + beanClassType.getSimpleSourceName() + WRAPPER_SUFFIX + ".class);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("return _beanWrapper;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		
		return true;
	}

	private void generateConstructor(SourceWriter sourceWriter, JClassType classType, String className) {

		sourceWriter.println("private " + BindingHolder.class.getSimpleName() + " " + BINDING_HOLDER_VAR + ";");
		
		sourceWriter.println("public " + className + "() {");
		sourceWriter.indent();
		sourceWriter.println("super();");
		
		BindingFieldsBase bindingFieldsBaseAnnotation = classType.getAnnotation(BindingFieldsBase.class);
		UpdateStrategy updateStrategy;
		if (bindingFieldsBaseAnnotation == null) {
			updateStrategy = UpdateStrategy.READ;
		} else {
			updateStrategy = bindingFieldsBaseAnnotation.updateStrategy();
		}

		bindingHolder = BINDING_HOLDER_VAR;
		String bindingHolderClassName;

		if(!validationEnabled) {
			bindingHolderClassName = BindingHolder.class.getSimpleName();
		} else {
			bindingHolderClassName = ValidatableBindingHolder.class.getName();
		}
		sourceWriter.println(bindingHolder + " = new " + bindingHolderClassName + "(" 
				+ UpdateStrategy.class.getSimpleName() + "." + updateStrategy.toString() + ", getBeanWrapper());");
		
		if(validationEnabled && !bindingFieldsBaseAnnotation.validationHighlighter().equals(ValidationHighligther.class)) {
			// set highlighter from annotation
			sourceWriter.println("((" + ValidatableBindingHolder.class.getName() + ")" + bindingHolder
					+ ").setHighlighter(new " + bindingFieldsBaseAnnotation.validationHighlighter().getName()
					+ "());");
		}
		
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
	
	private void generateFields(SourceWriter sourceWriter, JClassType classType) throws UnableToCompleteException {
		
		for(JField field : classType.getFields()) {
			BindingField bindingFieldAnnotation = field.getAnnotation(BindingField.class);
			if (bindingFieldAnnotation != null) {
				IBindingCreator<? extends Annotation> bindingGenerator = BindingCreatorFactory.getBindingCreator(field);
				
				if (bindingGenerator != null) {
					bindingGenerator.generateFields(field, bindingFieldAnnotation);
				}
			}
		}
	}
	
	private void generateOnLoadMethod(SourceWriter sourceWriter, JClassType classType) throws UnableToCompleteException {
		sourceWriter.println("@Override");
		sourceWriter.println("public void onLoad() {");
		sourceWriter.indent();
		sourceWriter.println("super.onLoad();");
				
		int count = 0;

		for(JField field : classType.getFields()) {
			BindingField bindingFieldAnnotation = field.getAnnotation(BindingField.class);
			if (bindingFieldAnnotation != null) {
				IBindingCreator<? extends Annotation> bindingGenerator = BindingCreatorFactory.getBindingCreator(field);
				
				if (bindingGenerator != null) {
					if (bindingGenerator.generateBinding(field, bindingFieldAnnotation)) {
						count++;
					}
				}
			}
		}
		
		if (count > 0) {
			sourceWriter.println("if (" + bindingHolder + ".getBean() != null) {");
			sourceWriter.indent();
			sourceWriter.println(bindingHolder + ".bind();");
			sourceWriter.outdent();
			sourceWriter.println("}");
		}
		
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
}