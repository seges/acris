package sk.seges.sesam.pap.configuration.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.ConfigurationProviderTypeElement;
import sk.seges.sesam.pap.configuration.model.SettingsTypeElement;

public abstract class AbstractConfigurationProviderProcessor extends MutableAnnotationProcessor {

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new ConfigurationProviderTypeElement(context)
		};
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		ArrayList<Element> elements = new ArrayList<Element>(getClassPathTypes().getElementsAnnotatedWith(Configuration.class, roundEnv));
		
		Collections.sort(elements, new Comparator<Element>() {

			@Override
			public int compare(Element o1, Element o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		
		FormattedPrintWriter pw = context.getPrintWriter();

		for (Element element: elements) {
			SettingsTypeElement settingsTypeElement = new SettingsTypeElement((DeclaredType)element.asType(), processingEnv);
			pw.println(settingsTypeElement, " get" + settingsTypeElement.getSimpleName() + "();");
			pw.println();
		}
	}
}