package sk.seges.acris.theme.pap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.theme.pap.accessor.MutableAnnotationAccessor;
import sk.seges.acris.theme.pap.accessor.ThemeElementsAccessor;
import sk.seges.acris.theme.pap.accessor.ThemeElementsAccessor.ThemeElementAccessor;
import sk.seges.acris.theme.pap.accessor.ThemeResourcesAccessor;
import sk.seges.acris.theme.pap.accessor.ThemeResourcesAccessor.ThemeResourceAccessor;
import sk.seges.acris.theme.pap.accessor.ThemeSupportAccessor;
import sk.seges.acris.theme.pap.accessor.UiFieldMutableAccessor;
import sk.seges.acris.theme.pap.configurer.ThemeProcessorConfigurer;
import sk.seges.acris.theme.pap.model.ThemeConfigurationType;
import sk.seges.acris.theme.pap.model.ThemeUiBinderType;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
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
		MutableDeclaredType outputType = context.getOutputType();

		ThemeSupportAccessor themeSupportAccessor = new ThemeSupportAccessor(element, processingEnv);
		
		ThemeUiBinderType themeUiBinderType = new ThemeUiBinderType(element.getSimpleName().toString(), context.getOutputType(), processingEnv);
		themeUiBinderType.annotateWith(new MutableAnnotationAccessor(UiTemplate.class, processingEnv).setValue(themeSupportAccessor.getUiTemplateName()));

		outputType.addNestedType(themeUiBinderType);
		
		//UiField annotation
		boolean provided = false;/*themeSupportAnnotation.field().provided()*/;
		
		outputType.addField((MutableVariableElement) processingEnv.getElementUtils().getParameterElement(Element.class, themeSupportAccessor.getElementName()).
						annotateWith(new UiFieldMutableAccessor(processingEnv).setProvided(provided)))
				  .addField(processingEnv.getElementUtils().getParameterElement(Element.class, "parentElement"));

		ThemeElementsAccessor themeElementsAccessor = new ThemeElementsAccessor(element, processingEnv);
		
		if (themeElementsAccessor.isValid()) {
			for (ThemeElementAccessor themeElementAccessor: themeElementsAccessor.getValue()) {
				outputType.addField((MutableVariableElement) processingEnv.getElementUtils().getParameterElement(Element.class, themeElementAccessor.getValue()).
						annotateWith(new UiFieldMutableAccessor(processingEnv).setProvided(provided)));
			}
		}
		
		ThemeResourcesAccessor themeResourcesAccessor = new ThemeResourcesAccessor(element, processingEnv);

		if (themeResourcesAccessor.isValid()) {
			for (ThemeResourceAccessor resource: themeResourcesAccessor.getValue()) {
				
				outputType.addField((MutableVariableElement) processingEnv.getElementUtils().getParameterElement(
							processingEnv.getTypeUtils().toMutableType(resource.getResourceClass()), resource.getName()).
						annotateWith(new UiFieldMutableAccessor(processingEnv).setProvided(resource.getUiField().isProvided())));
			}
		}

		HierarchyPrintWriter constructorPrintWriter = outputType.getConstructor().addModifier(Modifier.PUBLIC).getPrintWriter();
		
		constructorPrintWriter.println(themeUiBinderType.toString(ClassSerializer.SIMPLE, true), " uiBinder = ", GWT.class, ".create(", themeUiBinderType.toString(ClassSerializer.SIMPLE), ".class);");
		constructorPrintWriter.println("setElement(uiBinder.createAndBindUi(this));");
		constructorPrintWriter.println("parentElement = " +  themeSupportAccessor.getElementName() + ".getParentElement();");		

		MutableExecutableType getElementMethod = 
				processingEnv.getTypeUtils().getExecutable(processingEnv.getTypeUtils().toMutableType(com.google.gwt.user.client.Element.class), "getElement").
					addParameter(processingEnv.getElementUtils().getParameterElement(String.class, "name")).addModifier(Modifier.PUBLIC);

		outputType.addMethod(getElementMethod);
		
		HierarchyPrintWriter getElementMethodPrinter = getElementMethod.getPrintWriter();
		
		if (themeElementsAccessor.isValid()) {
			for (ThemeElementAccessor themeElementAccessor: themeElementsAccessor.getValue()) {
				getElementMethodPrinter.println("if (name.equals(\"" + themeElementAccessor.getValue() + "\")) {");
				getElementMethodPrinter.println("return " + themeElementAccessor.getValue() + ".cast();");
				getElementMethodPrinter.println("}");
			}
		}

		getElementMethodPrinter.println("if (name.equals(\"" + themeSupportAccessor.getElementName() + "\")) {");
		getElementMethodPrinter.println("return " + themeSupportAccessor.getElementName() + ".cast();");
		getElementMethodPrinter.println("}");
		getElementMethodPrinter.println("return null;");
	}
}