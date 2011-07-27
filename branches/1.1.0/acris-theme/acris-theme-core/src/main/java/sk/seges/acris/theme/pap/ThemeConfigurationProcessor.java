package sk.seges.acris.theme.pap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import sk.seges.acris.theme.client.annotation.ThemeConfiguration;
import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.rebind.ThemeComponentSelectorGenerator;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;


@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ThemeConfigurationProcessor extends AbstractProcessor {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(ThemeConfiguration.class.getCanonicalName());
		return annotations;
	}

	private String toFirstUppercase(String name) {
		if (name.length() == 1) {
			return name.toUpperCase();
		}
		name = name.toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (!roundEnv.processingOver()) {
			Set<? extends Element> configurations = roundEnv.getElementsAnnotatedWith(ThemeConfiguration.class);
			
			for (Element configurationElement: configurations) {

				Set<? extends Element> themeComponents = roundEnv.getElementsAnnotatedWith(ThemeSupport.class);

				if (themeComponents.size() == 0) {
					processingEnv.getMessager().printMessage(Kind.NOTE, "No themed components found. Skipping processor execution. ", configurationElement);
					return false;
				}

				ThemeConfiguration themeConfiguration = configurationElement.getAnnotation(ThemeConfiguration.class);

				processingEnv.getMessager().printMessage(Kind.NOTE, "Creating GWT module for theme " + themeConfiguration.themeName() + ".", configurationElement);

				PrintWriter pw = null;
				
				try {
					
					String packageName = processingEnv.getElementUtils().getPackageOf(configurationElement).getQualifiedName().toString();
					packageName = packageName.replace(".", "/");
					
					FileObject configurationFile = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "",  
							packageName + "/" + toFirstUppercase(themeConfiguration.themeName()) + "ThemeConfiguration.gwt.xml", configurationElement);
					OutputStream os = configurationFile.openOutputStream();
					
					pw = new PrintWriter(os);

					pw.println("<module>");
					pw.println("	<inherits name='com.google.gwt.user.User' />");
					pw.println("	<inherits name='sk.seges.acris.Theme' />");
					pw.println("");
					pw.println("	<set-property name=\"acristheme\" value=\"" + themeConfiguration.themeName() + "\" />");
					pw.println("");
					pw.println("	<generate-with class=\"" + ThemeComponentSelectorGenerator.class.getCanonicalName() + "\">");
					pw.println("		<any>");

					for (Element themeComponent: themeComponents) {
						ThemeSupport themeSupport = themeComponent.getAnnotation(ThemeSupport.class);

						TypeElement widgetClass = AnnotationClassPropertyHarvester.getTypeOfClassProperty(themeSupport, new AnnotationClassProperty<ThemeSupport>() {

							@Override
							public Class<?> getClassProperty(ThemeSupport annotation) {
								return annotation.widgetClass();
							}});
						pw.println("			<when-type-is class=\"" + widgetClass.toString() + "\" />");
					}

					pw.println("		</any>");
					pw.println("	</generate-with>");
					pw.println();
					pw.println("</module>");
					
					pw.flush();

				} catch (IOException e) {
					e.printStackTrace();
					processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to process element " + e.getMessage(), configurationElement);
				} finally {
					if (pw != null) {
						pw.close();
					}
				}
			}
		}
		return false;
	}
}