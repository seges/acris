package sk.seges.sesam.pap.converter.util;

import javax.annotation.processing.ProcessingEnvironment;

import sk.seges.sesam.core.pap.processor.ConfigurableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.MethodHelper;

public class ProjectNameResolver {

	private final ProcessingEnvironment processingEnv;
	
	public ProjectNameResolver(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	public String getName() {
			
		String projectName = processingEnv.getOptions().get(ConfigurableAnnotationProcessor.PROJECT_NAME_OPTION);
		
		int colonIndex = projectName.indexOf(":");
		
		if (colonIndex != -1) {
			projectName = projectName.substring(0, colonIndex);
		}
		
		String[] projectNameParts = projectName.split("-");
		
		projectName = "";
		
		for (String projectNamePart: projectNameParts) {
			projectName += MethodHelper.toMethod(projectNamePart);
		}
		
		return projectName;
	}
}