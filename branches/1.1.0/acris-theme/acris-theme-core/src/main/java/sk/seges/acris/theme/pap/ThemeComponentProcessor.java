package sk.seges.acris.theme.pap;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.theme.client.annotation.ThemeElements;
import sk.seges.acris.theme.client.annotation.ThemeElements.ThemeElement;
import sk.seges.acris.theme.client.annotation.ThemeResources;
import sk.seges.acris.theme.client.annotation.ThemeResources.ThemeResource;
import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.pap.util.AnnotationClassPropertyHarvester;
import sk.seges.acris.theme.pap.util.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ThemeComponentProcessor extends AbstractConfigurableProcessor {

	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(ThemeSupport.class.getCanonicalName());
		return annotations;
	}

	@Override
	protected Type[] getImports() {
		return new Type[] {
			Widget.class,
			UiBinder.class,
			Element.class,
			UiField.class,
			GWT.class,
			UiTemplate.class
		};
	}
	
	public static final ImmutableType getOutputClass(ImmutableType mutableType) {
		return mutableType.addClassSufix("Component");
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_SUPERCLASS:
			return new Type[] { Widget.class };
	}
		return super.getOutputDefinition(type, typeElement);
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] {
			getOutputClass(mutableType)
		};
	};
	
	@Override
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {

		String name = element.getSimpleName().toString();

		ThemeSupport themeSupportAnnotation = element.getAnnotation(ThemeSupport.class);

		UiTemplate uiTemplate = themeSupportAnnotation.template();
		String uiTemplateName = uiTemplate.value();
		
		if (uiTemplateName != null && !uiTemplateName.equals(Constants.NULL)) {
			pw.println("@" + UiTemplate.class.getSimpleName() + "(\"" + uiTemplateName + "\")");
		} else {
			pw.println("@" + UiTemplate.class.getSimpleName() + "(\"" + name + ".ui.xml\")");
		}
 		pw.println("interface " + name + UiBinder.class.getSimpleName() + " extends " + UiBinder.class.getSimpleName() + "<" + Element.class.getSimpleName() + ", " + outputClass.getSimpleName() + "> {}");
		pw.println();
				
		//UiField annotation
		boolean provided = false;/*themeSupportAnnotation.field().provided()*/;
		pw.println("@" + UiField.class.getSimpleName() + "(provided = " + provided + ")");
		
		String elementClassName = Element.class.getSimpleName();

		pw.println(elementClassName + " " + themeSupportAnnotation.elementName() + ";");
		pw.println();
		pw.println(Element.class.getSimpleName() + " parentElement;");
		pw.println();

		ThemeElements themeElementsAnnotation = element.getAnnotation(ThemeElements.class);
		
		if (themeElementsAnnotation != null && themeElementsAnnotation.value() != null && themeElementsAnnotation.value().length > 0) {
			for (ThemeElement themeElement: themeElementsAnnotation.value()) {
				pw.println("@" + UiField.class.getSimpleName() + "(provided = " + provided + ")");
				pw.println(elementClassName + " " + themeElement.value() + ";");
				pw.println();
			}
		}
		
		ThemeResources themeResourcesAnnotation = element.getAnnotation(ThemeResources.class);

		if (themeResourcesAnnotation != null && themeResourcesAnnotation.value() != null && themeResourcesAnnotation.value().length > 0) {
			for (ThemeResource resource: themeResourcesAnnotation.value()) {
				pw.println("@" + UiField.class.getSimpleName() + "(provided = " + resource.field().provided() + ")");
				pw.println(AnnotationClassPropertyHarvester.getTypeOfClassProperty(resource, new AnnotationClassProperty<ThemeResource>() {

					@Override
					public Class<?> getClassProperty(ThemeResource annotation) {
						return annotation.resourceClass();
					}
				} ).toString() + " " + resource.name() + ";");
				pw.println("");
			}
		}
		
		pw.println("public " + outputClass.getSimpleName() + "() {");
		pw.println(name + UiBinder.class.getSimpleName() + " uiBinder = " + GWT.class.getSimpleName() + ".create(" + name + UiBinder.class.getSimpleName() + ".class);");
		pw.println("setElement(uiBinder.createAndBindUi(this));");
		pw.println("parentElement = " +  themeSupportAnnotation.elementName() + ".getParentElement();");
		pw.println("}");
		pw.println("");
		pw.println("public " + com.google.gwt.user.client.Element.class.getCanonicalName() + " getElement(String name) {");

		if (themeElementsAnnotation != null && themeElementsAnnotation.value() != null && themeElementsAnnotation.value().length > 0) {
			for (ThemeElement themeElement: themeElementsAnnotation.value()) {
				pw.println("if (name.equals(\"" + themeElement.value() + "\")) {");
				pw.println("return " + themeElement.value() + ".cast();");
				pw.println("}");
			}
		}

		pw.println("if (name.equals(\"" + themeSupportAnnotation.elementName() + "\")) {");
		pw.println("return " + themeSupportAnnotation.elementName() + ".cast();");
		pw.println("}");
 
		pw.println("return null;");
		pw.println("}");
	}
}