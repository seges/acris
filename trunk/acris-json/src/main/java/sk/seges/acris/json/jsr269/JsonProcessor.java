package sk.seges.acris.json.jsr269;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

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

import sk.seges.acris.core.jsr269.AbstractConfigurableProcessor;
import sk.seges.acris.json.client.IJsonizer;
import sk.seges.acris.json.client.annotation.JsonObject;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions( { JsonProcessor.CONFIG_FILE_LOCATION })
public class JsonProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/json.properties";

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		annotations.add(JsonObject.class.getCanonicalName());
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		try {
			Name fqnElement = ((TypeElement) element).getQualifiedName();

			String fqn = fqnElement.toString() + "Jsonizer";
			String packageName = fqn.substring(0, fqn.lastIndexOf("."));
			String simpleName = element.getSimpleName() + "Jsonizer";

			JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(fqn, element);
			OutputStream os = createSourceFile.openOutputStream();
			PrintWriter pw = new PrintWriter(os);

			pw.println("package " + packageName + ";");
			pw.println();
			pw.println("public interface " + simpleName + " extends " + IJsonizer.class.getCanonicalName() + "<"
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