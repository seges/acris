package sk.seges.acris.theme.pap.specific;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.theme.pap.specific.AbstractComponentSpecificProcessor.Statement;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public interface ComponentSpecificProcessor {

    void init(ProcessingEnvironment processingEnv);

	boolean supports(TypeElement typeElement);
	
	void process(Statement statement, ThemeContext themeContext, FormattedPrintWriter pw);
	
	boolean isComponentMethod(ExecutableElement method);

	boolean ignoreMethod(ExecutableElement method);

}