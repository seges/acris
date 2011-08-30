package sk.seges.acris.theme.pap.specific;

import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.theme.pap.specific.AbstractComponentSpecificProcessor.Statement;

public interface ComponentSpecificProcessor {

    void init(ProcessingEnvironment processingEnv);

	boolean supports(TypeElement typeElement);
	
	void process(Statement statement, ThemeContext themeContext, PrintWriter pw);
	
	Type[] getImports();
	
	boolean isComponentMethod(ExecutableElement method);

	boolean ignoreMethod(ExecutableElement method);

}