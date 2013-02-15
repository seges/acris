package sk.seges.acris.binding.rebind.binding;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.client.annotations.BindingFieldsBase;
import sk.seges.acris.binding.client.annotations.ValidationStrategy;
import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.holder.ConfigurableBinding;
import sk.seges.acris.binding.client.holder.IBeanBindingHolder;
import sk.seges.acris.binding.client.holder.IBindingHolder;
import sk.seges.acris.binding.client.holder.validation.ValidatableBeanBinding;
import sk.seges.acris.binding.client.holder.validation.ValidatableBindingHolder;
import sk.seges.acris.binding.client.holder.validation.ValidationHighligther;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.binding.rebind.AbstractCreator;
import sk.seges.acris.binding.rebind.GeneratorException;
import sk.seges.acris.binding.rebind.binding.support.BindingCreatorFactory;
import sk.seges.acris.binding.rebind.binding.support.IBindingCreator;
import sk.seges.acris.binding.rebind.configuration.BindingNamingStrategy;
import sk.seges.acris.binding.rebind.loader.DefaultLoaderCreatorFactory;
import sk.seges.acris.binding.rebind.loader.EmptyLoaderCreator;
import sk.seges.acris.core.rebind.RebindUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.validation.client.InvalidConstraint;

public class BeanBindingCreator extends AbstractCreator {

	private static final String BINDING_HOLDER_VAR = "_bindingHolder";

	private String bindingHolder = null;

	private JClassType bindingBeanClassType;

	private boolean validationEnabled = false;

	private BindingNamingStrategy namingStrategy;

	public BeanBindingCreator(BindingNamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	/**
	 * Set default loader creator. Empty loader is default loader for x-To-Many
	 * bindings
	 */
	protected void setDefaultLoaderCreator() {
		DefaultLoaderCreatorFactory.setDefaultLoaderCreator(EmptyLoaderCreator.class);
	}

	protected String getOutputSimpleName() {
		return namingStrategy.getBeansBinderName(classType.getSimpleSourceName());
	}

	protected String[] getImports() throws UnableToCompleteException {
		String[] imports = new String[] { UpdateStrategy.class.getCanonicalName(),
				GWT.class.getCanonicalName(), BindingHolder.class.getCanonicalName(),
				BeanWrapper.class.getCanonicalName(), bindingBeanClassType.getQualifiedSourceName(),
				namingStrategy.getBeanWrapperName(bindingBeanClassType.getQualifiedSourceName()) };

		List<String> result = new ArrayList<String>();

		for (String importName : imports) {
			result.add(importName);
		}

		// Adding biding specific imports
		for (JField field : classType.getFields()) {
			BindingField bindingFieldAnnotation = field.getAnnotation(BindingField.class);
			if (bindingFieldAnnotation == null) {
				continue;
			}

			IBindingCreator<? extends Annotation> bindingGenerator = BindingCreatorFactory
					.getBindingCreator(field);

			if (bindingGenerator != null) {
				String[] bindingImports = bindingGenerator.getImports(field, bindingFieldAnnotation);

				for (String imp : bindingImports) {
					result.add(imp);
				}
			}
		}

		return result.toArray(new String[] {});
	}

	protected String[] getImplementedInterfaces() {
		if (validationEnabled) {
			return new String[] { ConfigurableBinding.class.getCanonicalName(),
					ValidatableBeanBinding.class.getName() + "<" + bindingBeanClassType.getName() + ">" };
		}

		return new String[] { ConfigurableBinding.class.getCanonicalName() };
	}

	protected String getSuperclassName() {
		return typeName;
	}

	@Override
	protected boolean initialize() throws UnableToCompleteException {

		try {
			bindingBeanClassType = RebindUtils.getGenericsFromInterfaceType(classType, typeOracle
					.findType(IBeanBindingHolder.class.getName()), 0);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to extract generic type class from interface");
			throw new UnableToCompleteException();
		}

		// initialize binding context for all related creators
		BindingCreatorFactory.setBindingContext(bindingBeanClassType, packageName, namingStrategy);

		BindingFieldsBase bindingFieldsBaseAnnotation = classType.getAnnotation(BindingFieldsBase.class);

		if (bindingFieldsBaseAnnotation == null) {
			return false;
		}

		validationEnabled = isValidationEnabled(bindingFieldsBaseAnnotation);

		setDefaultLoaderCreator();

		try {
			BindingCreatorFactory.fillSupportedTypes(context.getTypeOracle());
		} catch (GeneratorException e) {
			logger.log(Type.ERROR, e.getMessage());
			throw new UnableToCompleteException();
		}

		return true;
	}

	protected void doGenerate(SourceWriter sourceWriter) throws UnableToCompleteException {

		// initialize generator context for all related creators
		BindingCreatorFactory.setGeneratorContext(context, sourceWriter, logger);

		generateConstructor(sourceWriter, classType);
		generateConfigurableBindingImplementation(sourceWriter, bindingBeanClassType);
		generateBindingMethods(sourceWriter, bindingBeanClassType);
		generateValidationBeanBindingMethods(sourceWriter, classType, bindingBeanClassType);

		// initialize binding form for all related creators
		BindingCreatorFactory.setBindingHolder(bindingHolder);

		generateFields(sourceWriter, classType);

		generateOnLoadMethod(sourceWriter, classType);
	}

	private void generateConfigurableBindingImplementation(SourceWriter sw, JClassType beanClassType) {
		String parametrizedType = beanClassType.getSimpleSourceName();

		sw.println("@Override");
		sw.println("public " + IBindingHolder.class.getCanonicalName() + "<" + parametrizedType
				+ "> getHolder() {");
		sw.indent();
		sw.println("return " + BINDING_HOLDER_VAR + ";");
		sw.outdent();
		sw.println("}");

		sw.println("@Override");
		sw.println("public " + BeanWrapper.class.getSimpleName() + "<" + parametrizedType
				+ "> getBeanWrapper() {");
		sw.indent();
		sw.println("if (_beanWrapper == null) {");
		sw.indent();
		sw.println("_beanWrapper = GWT.create("
				+ namingStrategy.getBeanWrapperName(beanClassType.getSimpleSourceName()) + ".class);");
		sw.outdent();
		sw.println("}");
		sw.println("return _beanWrapper;");
		sw.outdent();
		sw.println("}");
	}

	protected Class<?> getValidatorBindingHolderClass() {
		return ValidatableBindingHolder.class;
	}
	
	private void generateValidationBeanBindingMethods(SourceWriter sw, JClassType classType,
			JClassType bindingBeanClassType) {
		if (!validationEnabled) {
			return;
		}

		sw.println("@Override");
		sw.println("public void highlightConstraints(" + Set.class.getName() + "<" + InvalidConstraint.class.getName()
				+ "<" + bindingBeanClassType.getSimpleSourceName() + ">> constraints) {");
		sw.indent();
		sw.println("((" + getValidatorBindingHolderClass().getName() + ")" + BINDING_HOLDER_VAR
				+ ").highlightConstraints(constraints);");
		sw.outdent();
		sw.println("}");
		sw.println();
		sw.println("@Override");
		sw.println("public void highlightConstraint(" + InvalidConstraint.class.getName() + "<"
				+ bindingBeanClassType.getSimpleSourceName() + "> constraint) {");
		sw.indent();
		sw.println("((" + getValidatorBindingHolderClass().getName() + ")" + BINDING_HOLDER_VAR
				+ ").highlightConstraint(constraint);");
		sw.outdent();
		sw.println("}");
		sw.println();
		sw.println("@Override");
		sw.println("public void clearHighlight() {");
		sw.indent();
		sw.println("((" + getValidatorBindingHolderClass().getName() + ")" + BINDING_HOLDER_VAR + ").clearHighlight();");
		sw.outdent();
		sw.println("}");
		sw.println();
		sw.println("@Override");
		sw.println("public void clearHighlight(" + Widget.class.getCanonicalName() + " widget) {");
		sw.indent();
		sw.println("((" + getValidatorBindingHolderClass().getName() + ")" + BINDING_HOLDER_VAR
				+ ").clearHighlight(widget);");
		sw.outdent();
		sw.println("}");
		sw.println();
		sw.println("@Override");
		sw.println("public " + Widget.class.getCanonicalName() + " getPropertyWidget(String property) {");
		sw.indent();
		sw.println("return ((" + getValidatorBindingHolderClass().getName() + ")" + BINDING_HOLDER_VAR
				+ ").getPropertyWidget(property);");
		sw.outdent();
		sw.println("}");
	}

	private boolean isValidationEnabled(BindingFieldsBase bindingFieldsBaseAnnotation) {
		return bindingFieldsBaseAnnotation.validationStrategy() != null
				&& !ValidationStrategy.NEVER.equals(bindingFieldsBaseAnnotation.validationStrategy());
	}

	private boolean generateBindingMethods(SourceWriter sourceWriter, JClassType beanClassType) {
		sourceWriter.println("private " + BeanWrapper.class.getSimpleName() + "<"
				+ beanClassType.getSimpleSourceName() + "> _beanWrapper;");
		sourceWriter.println("");
		sourceWriter.println("public void setBean(" + beanClassType.getSimpleSourceName() + " __bean) {");
		sourceWriter.indent();
		sourceWriter.println("if (__bean == null) {");
		sourceWriter.indent();
		sourceWriter.println("return;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("getBeanWrapper().setBeanWrapperContent(__bean);");
		sourceWriter.println(bindingHolder + ".setBean(__bean);");
		sourceWriter.println("super.setBean(__bean);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("public " + beanClassType.getSimpleSourceName() + " getBean() {");
		sourceWriter.indent();
		sourceWriter.println("return getBeanWrapper().getBeanWrapperContent();");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");

		return true;
	}

	private void generateConstructor(SourceWriter sourceWriter, JClassType classType) {

		sourceWriter.println("private " + BindingHolder.class.getSimpleName() + " " + BINDING_HOLDER_VAR
				+ ";");

		sourceWriter.println("public " + getOutputSimpleName() + "() {");
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

		if (!validationEnabled) {
			bindingHolderClassName = BindingHolder.class.getSimpleName();
		} else {
			bindingHolderClassName = getValidatorBindingHolderClass().getName();
		}
		sourceWriter.println(bindingHolder + " = new " + bindingHolderClassName + "("
				+ UpdateStrategy.class.getSimpleName() + "." + updateStrategy.toString()
				+ ", getBeanWrapper());");

		if (validationEnabled
				&& !bindingFieldsBaseAnnotation.validationHighlighter().equals(ValidationHighligther.class)) {
			// set highlighter from annotation
			sourceWriter.println("((" + getValidatorBindingHolderClass().getName() + ")" + bindingHolder
					+ ").setHighlighter(new " + bindingFieldsBaseAnnotation.validationHighlighter().getName()
					+ "());");
		}

		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	private void generateFields(SourceWriter sourceWriter, JClassType classType)
			throws UnableToCompleteException {

		for (JField field : classType.getFields()) {
			BindingField bindingFieldAnnotation = field.getAnnotation(BindingField.class);
			if (bindingFieldAnnotation != null) {
				IBindingCreator<? extends Annotation> bindingGenerator = BindingCreatorFactory
						.getBindingCreator(field);

				if (bindingGenerator != null) {
					bindingGenerator.generateFields(field, bindingFieldAnnotation);
				}
			}
		}
	}

	private void generateOnLoadMethod(SourceWriter sourceWriter, JClassType classType)
			throws UnableToCompleteException {
		sourceWriter.println("@Override");
		sourceWriter.println("public void onLoad() {");
		sourceWriter.indent();
		sourceWriter.println("super.onLoad();");

		int count = 0;

		for (JField field : classType.getFields()) {
			BindingField bindingFieldAnnotation = field.getAnnotation(BindingField.class);
			if (bindingFieldAnnotation != null) {
				IBindingCreator<? extends Annotation> bindingGenerator = BindingCreatorFactory
						.getBindingCreator(field);

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