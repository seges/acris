/**
 * 
 */
package sk.seges.acris.binding.jsr269;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.annotation.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.core.jsr269.AbstractConfigurableProcessor;

/**
 * Generates bean wrapper interfaces for all relevant classes. The definition of which classes to process is following
 * the rule:
 * <ul>
 * <li>by default {@link sk.seges.acris.binding.client.annotations.BeanWrapper} annotated classes are taken</li>
 * <li>addition configuration is read from project's META-INF/bean-wrapper.properties file</li>
 * </ul>
 * 
 * @author eldzi
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions( { BeanWrapperProcessor.CONFIG_FILE_LOCATION })
public class BeanWrapperProcessor extends AbstractConfigurableProcessor {
	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/bean-wrapper.properties";

	public static final String BEAN_WRAPPER_SUFFIX = "BeanWrapper";
	
	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		annotations.add(sk.seges.acris.binding.client.annotations.BeanWrapper.class.getCanonicalName());
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		try {
			Name fqnElement = ((TypeElement) element).getQualifiedName();

			String fqn = fqnElement.toString() + BEAN_WRAPPER_SUFFIX;
			String packageName = fqn.substring(0, fqn.lastIndexOf("."));
			String simpleName = element.getSimpleName() + BEAN_WRAPPER_SUFFIX;

			JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(fqn, element);
			OutputStream os = createSourceFile.openOutputStream();
			PrintWriter pw = new PrintWriter(os);

			pw.println("package " + packageName + ";");
			pw.println();
			pw.println("@" + Generated.class.getCanonicalName() + "(\"" + BeanWrapperProcessor.class.getCanonicalName()
					+ "\")");
			pw.println("public interface " + simpleName + " extends " + BeanWrapper.class.getCanonicalName() + "<"
					+ fqnElement.toString() + "> {");
			pw.println("}");
			pw.flush();
			pw.close();

		} catch (IOException e) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to process element = ", element);
		}

		return true;
	}
}