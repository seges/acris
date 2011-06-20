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

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.client.annotation.ThemeSupport.Resource;
import sk.seges.acris.theme.pap.util.AnnotationClassPropertyHarvester;
import sk.seges.acris.theme.pap.util.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.core.pap.model.api.MutableType;
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
	
	public static final MutableType getOutputClass(MutableType mutableType) {
		return mutableType.addClassSufix("Component");
	}

	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {

		switch (type) {
			case OUTPUT_SUPERCLASS:
				return new Type[] { Widget.class };
		}
		
		return super.getConfigurationTypes(type, typeElement);
	}

	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
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
 		pw.println("interface " + name + "UiBinder extends " + UiBinder.class.getSimpleName() + "<" + Element.class.getSimpleName() + ", " + outputClass.getSimpleName() + "> {}");
		pw.println();
				
		//UiField annotation
		boolean provided = themeSupportAnnotation.field().provided();
		pw.println("@" + UiField.class.getSimpleName() + "(provided = " + provided + ")");
		
		String elementClassName = Element.class.getSimpleName();

		pw.println(elementClassName + " " + themeSupportAnnotation.elementName() + ";");
		pw.println();
		pw.println(Element.class.getSimpleName() + " parentElement;");
		pw.println();
		
		if (themeSupportAnnotation.resources() != null) {
			for (Resource resource: themeSupportAnnotation.resources()) {
				pw.println("@" + UiField.class.getSimpleName() + "(provided = " + resource.field().provided() + ")");
				pw.println(AnnotationClassPropertyHarvester.getTypeOfClassProperty(resource, new AnnotationClassProperty<Resource>() {

					@Override
					public Class<?> getClassProperty(Resource annotation) {
						return annotation.resourceClass();
					}
				} ).toString() + " " + resource.name() + ";");
				pw.println("");
			}
		}
		
		pw.println("public " + outputClass.getSimpleName() + "() {");
		pw.println(name + "UiBinder uiBinder = " + GWT.class.getSimpleName() + ".create(" + name + "UiBinder.class);");
		pw.println("setElement(uiBinder.createAndBindUi(this));");
		pw.println("parentElement = " +  themeSupportAnnotation.elementName() + ".getParentElement();");
		pw.println("}");
	}
}