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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import sk.seges.acris.core.jsr269.AbstractConfigurableProcessor;
import sk.seges.acris.json.client.annotation.JsonObject;
import sk.seges.acris.json.client.data.IJsonObject;

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
			
			String packageName = fqnElement.toString().substring(0, fqnElement.toString().lastIndexOf("."));
			
			Element enclosingElement = element.getEnclosingElement();
			while (enclosingElement != null) {
				if (!enclosingElement.getKind().equals(ElementKind.CLASS) && !enclosingElement.getKind().equals(ElementKind.INTERFACE)) {
					enclosingElement = null;
				} else {
					String enclosingName = ((TypeElement) enclosingElement).getQualifiedName().toString();
					packageName = enclosingName.substring(0, enclosingName.lastIndexOf("."));
					enclosingElement = enclosingElement.getEnclosingElement();
				}
			}
//			Name fqnSimpleName = ((TypeElement) element).getSimpleName();
			
//			String packageName = fqnElement.toString().substring(0, fqnElement.toString().lastIndexOf(".")) + ".json";
//			packageName = packageName.toLowerCase();
			String simpleName = fqnElement.toString().replace(packageName, "").replace(".", "") + "Jsonizer";

			JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + simpleName, element);
			OutputStream os = createSourceFile.openOutputStream();
			PrintWriter pw = new PrintWriter(os);

			pw.println("package " + packageName + ";");
			pw.println();
			pw.println("public interface " + simpleName + " extends " + IJsonObject.class.getCanonicalName() + "<"
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