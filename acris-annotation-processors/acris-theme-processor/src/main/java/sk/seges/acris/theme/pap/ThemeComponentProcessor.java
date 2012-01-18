package sk.seges.acris.theme.pap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.theme.client.annotation.ThemeElements;
import sk.seges.acris.theme.client.annotation.ThemeElements.ThemeElement;
import sk.seges.acris.theme.client.annotation.ThemeResources;
import sk.seges.acris.theme.client.annotation.ThemeResources.ThemeResource;
import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.pap.configurer.ThemeProcessorConfigurer;
import sk.seges.acris.theme.pap.model.ThemeConfigurationType;
import sk.seges.acris.theme.pap.model.ThemeUiBinderType;
import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.printer.TypePrinter;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ThemeComponentProcessor extends MutableAnnotationProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ThemeProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new ThemeConfigurationType(context.getTypeElement(), processingEnv).getThemeComponent()
		};
	}
	
	@Override
	protected void processElement(ProcessorContext context) {

		TypeElement element = context.getTypeElement();
		FormattedPrintWriter pw = context.getPrintWriter();
		
		String name = element.getSimpleName().toString();

		ThemeSupport themeSupportAnnotation = element.getAnnotation(ThemeSupport.class);

		UiTemplate uiTemplate = themeSupportAnnotation.template();
		String uiTemplateName = uiTemplate.value();
		
		if (uiTemplateName != null && !uiTemplateName.equals(Constants.NULL)) {
			pw.println("@", UiTemplate.class, "(\"" + uiTemplateName + "\")");
		} else {
			pw.println("@", UiTemplate.class, "(\"" + name + ".ui.xml\")");
		}
		
		ThemeUiBinderType themeUiBinderType = new ThemeUiBinderType(name, context.getOutputType(), processingEnv);
		
		new TypePrinter(pw).printTypeDefinition(null, themeUiBinderType);
 		pw.println(" {}");
		pw.println();
				
		//UiField annotation
		boolean provided = false;/*themeSupportAnnotation.field().provided()*/;
		pw.println("@", UiField.class, "(provided = " + provided + ")");
		
		pw.println(Element.class, " " + themeSupportAnnotation.elementName() + ";");
		pw.println();
		pw.println(Element.class, " parentElement;");
		pw.println();

		ThemeElements themeElementsAnnotation = element.getAnnotation(ThemeElements.class);
		
		if (themeElementsAnnotation != null && themeElementsAnnotation.value() != null && themeElementsAnnotation.value().length > 0) {
			for (ThemeElement themeElement: themeElementsAnnotation.value()) {
				pw.println("@", UiField.class.getSimpleName(), "(provided = " + provided + ")");
				pw.println(Element.class, " " + themeElement.value() + ";");
				pw.println();
			}
		}
		
		ThemeResources themeResourcesAnnotation = element.getAnnotation(ThemeResources.class);

		if (themeResourcesAnnotation != null && themeResourcesAnnotation.value() != null && themeResourcesAnnotation.value().length > 0) {
			for (ThemeResource resource: themeResourcesAnnotation.value()) {
				pw.println("@", UiField.class, "(provided = " + resource.field().provided() + ")");
				pw.println(AnnotationClassPropertyHarvester.getTypeOfClassProperty(resource, new AnnotationClassProperty<ThemeResource>() {

					@Override
					public Class<?> getClassProperty(ThemeResource annotation) {
						return annotation.resourceClass();
					}
				} ), " " + resource.name() + ";");
				pw.println("");
			}
		}
		
		pw.println("public " + context.getOutputType().getSimpleName() + "() {");
		pw.println(themeUiBinderType.toString(ClassSerializer.SIMPLE, true), " uiBinder = ", GWT.class, ".create(", themeUiBinderType.toString(ClassSerializer.SIMPLE), ".class);");
		pw.println("setElement(uiBinder.createAndBindUi(this));");
		pw.println("parentElement = " +  themeSupportAnnotation.elementName() + ".getParentElement();");
		pw.println("}");
		pw.println("");
		pw.println("public ", com.google.gwt.user.client.Element.class, " getElement(String name) {");

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