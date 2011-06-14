/**
 * 
 */
package sk.seges.acris.widget.rebind.table;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sk.seges.acris.callbacks.client.CallbackAdapter;
import sk.seges.acris.core.rebind.RebindUtils;
import sk.seges.acris.widget.client.advanced.EnumListBoxWithValue;
import sk.seges.acris.widget.client.i18n.DynamicTranslator;
import sk.seges.acris.widget.client.loader.FreeServiceAwareLoader;
import sk.seges.acris.widget.client.table.BeanTable;
import sk.seges.acris.widget.client.table.BeanTable.DomainObjectProperty;
import sk.seges.acris.widget.client.table.BeanTable.FilterEnumProperty;
import sk.seges.acris.widget.client.table.BeanTable.FilterProperty;
import sk.seges.acris.widget.client.table.FreeSpecLoader;
import sk.seges.acris.widget.client.table.SpecColumn;
import sk.seges.acris.widget.client.table.SpecParams;
import sk.seges.sesam.dao.Criterion;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.javac.typemodel.JDummyClassType;
import com.google.gwt.dev.javac.typemodel.JMethodInstancer;
import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.gen2.table.client.CellEditor;
import com.google.gwt.gen2.table.client.CellRenderer;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * @author eldzi
 */
public class BeanTableSpecCreator {
	private static final String GEN_SPEC_SUFFIX = "Gen";
	private static final String GEN_TABLE_SUFFIX = "TableGen";

	private TreeLogger logger;
	private GeneratorContext context;
	private TypeOracle typeOracle;
	private String typeName;

	protected JClassType classType;
//	private SpecLoader loaderParams;
	protected SpecParams specParams;
	protected JClassType beanType;
	protected String beanTypeName;

	public BeanTableSpecCreator(TreeLogger logger, GeneratorContext context, String typeName) {
		this.logger = logger;
		this.context = context;
		this.typeOracle = context.getTypeOracle();
		this.typeName = typeName;
	}

	protected JClassType getClassType() {
		return this.classType;
	}
	
	protected String getBeanTypeName() {
		return this.beanTypeName;
	}
	
	public String createSpec() {
		try {
			classType = typeOracle.getType(typeName);
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Unable to find type name = " + typeName, e);
			return null;
		}

		specParams = classType.getAnnotation(SpecParams.class);
		if (specParams == null) {
			// create default spec. of params
			specParams = new SpecParams() {
				@Override
				public Class<? extends Annotation> annotationType() {
					return SpecParams.class;
				}

				@Override
				public boolean onlySpec() {
					return false;
				}

				@Override
				public String idProperty() {
					return "id";
				}
			};
		}

		beanType = ((com.google.gwt.core.ext.typeinfo.JParameterizedType) classType
				.getImplementedInterfaces()[0]).getTypeArgs()[0];
		beanTypeName = beanType.getQualifiedSourceName();
//		loaderParams = classType.getAnnotation(SpecLoader.class);

		SourceWriter source = getSourceWriter(classType, specParams.onlySpec());

		JMethod[] methods = classType.getMethods();
		methods = retrieveMethodsFromBeanIfNeeded(methods);

		generateMessagesClass(methods);

		if (source != null) {
			if (logger.isLoggable(Type.INFO)) {
				logger.log(Type.INFO, "Generating table spec for " + beanTypeName + ", source = " + source);
			}
			if (specParams.onlySpec()) {
				generateTableSpecification(source, methods);
			} else {
				generateTable(source, methods);
			}
		}
		return getFullReturnName();
	}

	private JMethod[] retrieveMethodsFromBeanIfNeeded(JMethod[] methods) {
		if (methods == null || methods.length == 0) {
			List<JMethod> listOfMethods = new LinkedList<JMethod>();
			JDummyClassType dummy = new JDummyClassType();
			Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
			com.google.gwt.dev.javac.typemodel.JTypeParameter[] jtypeParameters = new com.google.gwt.dev.javac.typemodel.JTypeParameter[0];
			for (JField field : beanType.getFields()) {
				if (!field.isTransient() && !field.isStatic()) {
					JMethod method = JMethodInstancer.instanceMethod(dummy, field.getName(), annotations,
							jtypeParameters);
					listOfMethods.add(method);
				}
			}
			methods = listOfMethods.toArray(methods);
		}
		return methods;
	}

	private void generateTableSpecification(SourceWriter source, JMethod[] methods) {
		source.println("public void initialize(BeanTable<" + beanTypeName + "> beanTable) {");
		putInitializationPart(source, "beanTable", methods);

		source.println("}");

		// dummy method implementation because we inherit the interface - no
		// usage yet
		for (JMethod method : methods) {
			source.println("public void " + method.getName() + "() {}");
		}

		source.commit(logger);
	}

	private String getFullReturnName() {
		return classType.getPackage().getName() + "." + getSimpleReturnName();
	}

	private String getSimpleReturnName() {
		return classType.getSimpleSourceName() + (specParams.onlySpec() ? GEN_SPEC_SUFFIX : GEN_TABLE_SUFFIX);
	}

	private void generateTable(SourceWriter source, JMethod[] methods) {
		source.println("public " + classType.getSimpleSourceName() + GEN_TABLE_SUFFIX + "() {");
		source.indent();
		putInitializationPart(source, null, methods);
		source.outdent();
		source.println("}");
		source.println();
		source.println("@Override");
		source.println("protected String getIdProperty() {");
		source.println("	return \"" + specParams.idProperty() + "\";");
		source.println("}");
		source.println();
		source.println("@Override");
		source.println("protected Class<?> getProjectableResult() {");
		source.println("	return " + beanTypeName + ".class;");
		source.println("}");

		source.commit(logger);
	}

	private String correctAffectedBean(String affectedBean) {
		return (affectedBean == null ? "" : affectedBean + ".");
	}

	private String getDynamicTranslatorInitialization(Class<? extends ConstantsWithLookup>[] messagesClasses) {
		if (messagesClasses == null
				|| messagesClasses.length < 1
				|| (messagesClasses.length == 1)
				&& (ConstantsWithLookup.class.getCanonicalName()
						.equals(messagesClasses[0].getCanonicalName())))
			return null;
		StringBuffer classes = null;
		for (Class<? extends ConstantsWithLookup> messagesClass : messagesClasses) {
			String classConstructor = "(" + messagesClass.getName() + ") GWT.create("
					+ messagesClass.getName() + ".class)";
			if (classes == null) {
				classes = new StringBuffer(classConstructor);
			} else {
				classes.append("," + classConstructor);
			}
		}
		if (classes == null)
			return null;

		return "final " + DynamicTranslator.class.getName() + " dynamicTranslator = new "
				+ DynamicTranslator.class.getName() + "(" + classes + ");";
	}

	private void putInitializationPart(SourceWriter source, String affectedBean, JMethod[] methods) {
		putLoaderInitialization(source, correctAffectedBean(affectedBean));

		if (methods.length > 0) {
			// source.println("AbstractColumnDefinition<" + beanTypeName +
			// ", ?> columnDefinition;");
			source.println("AbstractColumnDefinition columnDefinition;");
			source.println("Criterion filterable;");
			source.println(messagesTypeName() + " msgs = GWT.create(" + messagesTypeName() + ".class);");
		}

		for (JMethod method : methods) {
			putColumnDefinition(source, correctAffectedBean(affectedBean), method);
		}
	}

	/**
	 * override this, if special ServiceAwareLoader is needed
	 * @param source
	 * @param affectedBean
	 */
	protected void putLoaderInitialization(SourceWriter source, String affectedBean) {
		FreeSpecLoader loaderParams = classType.getAnnotation(FreeSpecLoader.class);
		if (loaderParams != null) {
			String loaderClassName = loaderParams.serviceClass().getName();

			source.println("	" + affectedBean + "setLoader(new "
					+ FreeServiceAwareLoader.class.getCanonicalName() + "<List<" + beanTypeName + ">, "
					// + loaderClassName + "Async>(\"" +
					// loaderParams.serviceChocolate() + "\") {"); - acrisova
					+ loaderClassName + "Async>() {"); // acris-os
			source.println("		@Override");
			source.println("		protected void load(" + loaderClassName + "Async service, "
					+ Page.class.getName() + " page,");
			source.println("				" + CallbackAdapter.class.getName() + "<" + PagedResult.class.getName()
					+ "<List<" + beanTypeName + ">>> callback) {");
			source.println("			service." + loaderParams.loaderMethodName() + "(page, callback);");
			source.println("		}");
			source.println("		@Override");
			source.println("		protected " + loaderClassName + "Async getService() {");
			source.println("			" + loaderClassName + "Async service = GWT.create(" + loaderClassName
					+ ".class);");
			source.println("			((" + ServiceDefTarget.class.getCanonicalName()
					+ ")service).setServiceEntryPoint(\"" + loaderParams.serviceEntryPoint() + "\");");
			source.println("			return service;");
			source.println("		}");
			source.println("	});");
			source.println();
		}
	}

	private void putColumnDefinition(SourceWriter source, String affectedBean, JMethod method) {
		SpecColumn specColumn = method.getAnnotation(SpecColumn.class);
		String field;
		String propertyValueType = null;
		boolean isTranslatable = false;
		String dynamicTranslatorInit = null;
		if (specColumn != null && !"".equals(specColumn.field())) {
			field = specColumn.field();
			dynamicTranslatorInit = getDynamicTranslatorInitialization(specColumn.messagesClasses());
			if (dynamicTranslatorInit != null && dynamicTranslatorInit.length() > 1) {
				isTranslatable = true;
				propertyValueType = String.class.getSimpleName();
			}
		} else {
			field = method.getName();
		}

		final String DOT_STRING = ".";
		String propertyField = null;
		JType fieldType = null;
		boolean hasAssociation = field.contains(DOT_STRING);
		try {
			if (hasAssociation) {
				String directField = field.substring(0, field.indexOf(DOT_STRING));
				propertyField = field.substring(field.indexOf(DOT_STRING) + 1);
				field = directField;

				fieldType = RebindUtils.getDeclaredFieldType(beanType, specColumn.field());
			} else {
				fieldType = RebindUtils.getDeclaredFieldType(beanType, field);
			}
		} catch (NotFoundException e) {
			throw new RuntimeException("Unable to retrieve type for field = " + field, e);
		}

		JMethod getter = null;
		JMethod setter = null;
		try {
			getter = RebindUtils.getGetter(beanType, field);
			setter = RebindUtils.getSetter(beanType, field, fieldType);
		} catch (NotFoundException e) {
			throw new RuntimeException("Unable to retrieve getter for field = " + field, e);
		}

		if (!isTranslatable)
			propertyValueType = fieldType.getQualifiedSourceName();

		if (dynamicTranslatorInit != null && dynamicTranslatorInit.length() > 1)
			source.println("	" + dynamicTranslatorInit);
		source.println("columnDefinition = new AbstractColumnDefinition<" + beanTypeName + ", "
				+ propertyValueType + ">() {");
		source.println("	@Override");
		source.println("	public " + propertyValueType + " getCellValue(" + beanTypeName + " rowValue) {");
		if (!hasAssociation) {
			source.println("		" + putCellValue("rowValue." + getter.getName() + "()", isTranslatable));
		} else {
			String propertyFieldGetter = null;
			try {
				propertyFieldGetter = RebindUtils.getGetterForMoreDotsInBeanTable(beanType, specColumn
						.field());
			} catch (NotFoundException e) {
				throw new RuntimeException("Unable to retrieve getter for field = " + field, e);
			}
			source.println("		" + putCellValue("rowValue." + propertyFieldGetter + "()", isTranslatable));
		}
		source.println("	}");
		source.println();
		source.println("	@Override");
		source.println("	public void setCellValue(" + beanTypeName + " rowValue, " + propertyValueType
				+ " cellValue) {");
		if (setter != null && propertyValueType.equals(fieldType.getQualifiedSourceName())) {
			source.indent();
			source.println("rowValue." + setter.getName() + "(cellValue);");
			source.outdent();
		}
		source.println("	}");
		source.println();
		source.println("};");

		source
				.println("columnDefinition.setColumnProperty(DomainObjectProperty.TYPE, new DomainObjectProperty(\""
						+ field + (propertyField == null ? "" : "." + propertyField) + "\"));");

		if (specColumn != null) {
			if (specColumn.filterWidgetType() != null && !specColumn.filterWidgetType().equals(Widget.class)) {
				String filterOperation = specColumn.filterOperation();
				if (filterOperation == null) {
					throw new RuntimeException("Provide filter operation on field " + field);
				}
				source.println("filterable = Filter." + filterOperation + "(\"" + field
						+ (propertyField == null ? "" : "." + propertyField) + "\");");
				if (specColumn.filterWidgetType().equals(EnumListBoxWithValue.class)) {
					String enumValues = Arrays.class.getName() + ".asList("
							+ fieldType.getQualifiedSourceName() + ".values())";
					if (isTranslatable) {
						source.println(Map.class.getName() + "<" + fieldType.getQualifiedSourceName()
								+ ", String> enumMap = new " + HashMap.class.getName() + "<"
								+ fieldType.getQualifiedSourceName() + ", String>();");
						source.println("for(" + fieldType.getQualifiedSourceName() + " enum1 : " + enumValues
								+ ") {");
						source.indentln("enumMap.put(enum1, dynamicTranslator.translate(enum1.name()));");
						source.println("}");
						enumValues = "enumMap";
					}
					source
							.println("columnDefinition.setColumnProperty(FilterProperty.TYPE, new FilterEnumProperty("
									+ specColumn.filterWidgetType().getName()
									+ ".class, filterable, "
									+ fieldType.getQualifiedSourceName() + ".class, " + enumValues + "));");

				} else {
					source
							.println("columnDefinition.setColumnProperty(FilterProperty.TYPE, new FilterProperty("
									+ specColumn.filterWidgetType().getName() + ".class, filterable));");
				}
			}
			if (specColumn.editor() != null && !specColumn.editor().equals(CellEditor.class)) {
				source.println("columnDefinition.setCellEditor(new " + specColumn.editor().getCanonicalName()
						+ "());");
			}

			if (specColumn.renderer() != null && !specColumn.renderer().equals(CellRenderer.class)) {
				source.println("columnDefinition.setCellRenderer(new "
						+ specColumn.renderer().getCanonicalName() + "());");
			}
		}

		source.println(affectedBean + "addColumn(msgs." + method.getName() + "(), columnDefinition);");
		source.println();
	}

	private String putCellValue(String line, Boolean isTranslatable) {
		if (isTranslatable)
			return "return dynamicTranslator.translate(" + line + ");";
		else
			return "return " + line + ";";
	}

	/**
	 * SourceWriter instantiation. Return null if the resource already exist.
	 * 
	 * @return sourceWriter
	 */
	public SourceWriter getSourceWriter(JClassType classType, boolean onlySpec) {
		String packageName = classType.getPackage().getName();
		String simpleName = getSimpleReturnName();
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);

		composer.addImport(List.class.getCanonicalName());
//		if (loaderParams != null) {
//			//composer.addImport("sk.seges.acris.loader.ServiceAwareLoader");
//			composer.addImport(FreeServiceAwareLoader.class.getCanonicalName());
//		}
		composer.addImport(DomainObjectProperty.class.getCanonicalName());
		composer.addImport(CallbackAdapter.class.getCanonicalName());
		composer.addImport(BeanTable.class.getCanonicalName());
		composer.addImport(FilterProperty.class.getCanonicalName());
		composer.addImport(FilterEnumProperty.class.getCanonicalName());
		composer.addImport(Criterion.class.getCanonicalName());
		composer.addImport(Filter.class.getCanonicalName());
		composer.addImport(Page.class.getCanonicalName());
		composer.addImport(PagedResult.class.getCanonicalName());
		composer.addImport(GWT.class.getCanonicalName());
		composer.addImport(AbstractColumnDefinition.class.getCanonicalName());

		if (onlySpec) {
			composer.addImplementedInterface(classType.getName());
		} else {
			// TODO: how to add parametrized class?
			composer.setSuperclass("BeanTable<" + beanTypeName + ">");
		}

		PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
		if (logger.isLoggable(Type.DEBUG)) {
			logger.log(Type.DEBUG, "Creating source writer for " + beanTypeName + ", onlySpec = " + onlySpec
					+ ", classType = " + classType + ", packageName = " + packageName + ", simpleName = "
					+ simpleName + ", printWriter = " + printWriter);
		}
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}

	private void generateMessagesClass(JMethod[] methods) {
		String packageName = classType.getPackage().getName();
		String simpleName = messagesTypeName();

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
		composer.makeInterface();

		composer.addImplementedInterface(Constants.class.getCanonicalName());

		PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
		if (logger.isLoggable(Type.DEBUG)) {
			logger.log(Type.DEBUG, "Creating messages source writer for " + simpleName + ", packageName = "
					+ packageName + ", simpleName = " + simpleName + ", printWriter = " + printWriter);
		}
		if (printWriter == null) {
			return;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			for (JMethod method : methods) {
				sw.println("String " + method.getName() + "();");
			}
			sw.commit(logger);
		}
	}

	private String messagesTypeName() {
		return classType.getSimpleSourceName() + "Messages";
	}
}
