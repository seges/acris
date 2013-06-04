package sk.seges.acris.widget.rebind;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.widget.client.uibinder.DynamicUiBinder;
import sk.seges.acris.widget.client.uibinder.DynamicUiPanel;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * @author ladislav.gazo
 */
public class DynamicUiBinderCreator {
	private static final String SUFFIX = "UiDynImpl";

	private TreeLogger logger;
	private GeneratorContext context;
	private TypeOracle typeOracle;
	private String typeName;

	protected JClassType classType;

	public DynamicUiBinderCreator(TreeLogger logger, GeneratorContext context, String typeName) {
		this.logger = logger;
		this.context = context;
		this.typeOracle = context.getTypeOracle();
		this.typeName = typeName;
	}

	public String create() {
		try {
			classType = typeOracle.getType(typeName);
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Unable to find type name = " + typeName, e);
			return null;
		}

		JClassType rootObject = ((com.google.gwt.core.ext.typeinfo.JParameterizedType) classType
				.getImplementedInterfaces()[0]).getTypeArgs()[0];

		JClassType owner = ((com.google.gwt.core.ext.typeinfo.JParameterizedType) classType.getImplementedInterfaces()[0])
				.getTypeArgs()[1];

		SourceWriter source = getSourceWriter(classType, rootObject, owner);

		if (source != null) {
			if (logger.isLoggable(Type.INFO)) {
				logger.log(Type.INFO, "Generating dynamic UI for rootObject = " + rootObject + ", owner = " + owner
						+ ", source = " + source);
			}

			source.println("@Override");
			source.println("protected void assign(Element child, " + owner.getQualifiedBinaryName() + " owner) {");
			source.indent();

			UiField fieldAnnotation;

			Map<JType, JMethod> creationMapping = new HashMap<JType, JMethod>();

			UiFactory factoryAnnotation;
			for (JMethod method : owner.getMethods()) {
				factoryAnnotation = method.getAnnotation(UiFactory.class);

				if (factoryAnnotation == null) {
					continue;
				}

				creationMapping.put(method.getReturnType(), method);
			}

			int i = 0;
			for (JField field : owner.getFields()) {
				fieldAnnotation = field.getAnnotation(UiField.class);

				if (fieldAnnotation == null) {
					continue;
				}

				writeAssignemntStep(source, field, i, creationMapping);
				i++;
			}

			if(i > 0) { 
				source.println("}");
			}
			source.outdent();
			source.println("}");
			
			source.commit(logger);
		}

		return getFullReturnName();
	}

	private void writeAssignemntStep(SourceWriter source, JField field, int i, Map<JType, JMethod> creationMapping) {
		source.println((i == 0 ? "if" : "} else if") + "(\"" + field.getName() + "\".equals(getFieldName(child))) {");
		source.indent();
		source.println("owner." + field.getName() + " = " + createCreator(field, creationMapping) + ";");
		source.outdent();
	}

	private String createCreator(JField field, Map<JType, JMethod> creationMapping) {
		if (creationMapping.containsKey(field.getType())) {
			return "owner." + creationMapping.get(field.getType()).getName() + "(child)";
		} else {
			return field.getType().getQualifiedBinaryName() + ".wrap(child)";
		}
	}

	private String getSimpleReturnName() {
		return classType.getSimpleSourceName() + SUFFIX;
	}

	private String getFullReturnName() {
		return classType.getPackage().getName() + "." + getSimpleReturnName();
	}

	public SourceWriter getSourceWriter(JClassType classType, JClassType rootObject, JClassType owner) {
		String packageName = classType.getPackage().getName();
		String simpleName = getSimpleReturnName();
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);

		composer.addImport(Element.class.getCanonicalName());

		composer.addImplementedInterface(classType.getQualifiedSourceName());
		composer.addImport(DynamicUiBinder.class.getCanonicalName());
		composer.setSuperclass(DynamicUiPanel.class.getCanonicalName() + "<" + rootObject.getQualifiedBinaryName()
				+ ", " + owner.getQualifiedBinaryName() + ">");

		PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
		if (logger.isLoggable(Type.DEBUG)) {
			logger.log(Type.DEBUG, "Creating source writer for " + ", classType = " + classType + ", packageName = "
					+ packageName + ", simpleName = " + simpleName + ", printWriter = " + printWriter);
		}
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}

}
