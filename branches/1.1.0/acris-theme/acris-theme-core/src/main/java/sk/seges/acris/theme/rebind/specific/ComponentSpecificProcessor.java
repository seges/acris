package sk.seges.acris.theme.rebind.specific;

import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.rebind.specific.AbstractComponentSpecificProcessor.Statement;

public interface ComponentSpecificProcessor {

    void init(ProcessingEnvironment processingEnv);

	boolean supports(TypeElement typeElement);
	
	void process(Statement statement, ThemeSupport themeSupport, PrintWriter pw);
	
	Type[] getImports();
}