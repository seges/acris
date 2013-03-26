package sk.seges.sesam.spring;

import org.springframework.context.ApplicationContext;

public class ParametrizedAnnotationConfigContextLoader extends AnnotationConfigContextLoader {

	private Class<?> configurationClass;
	
	public ParametrizedAnnotationConfigContextLoader(Class<?> configurationClass) {
		this.configurationClass = configurationClass;
	}
	
	public ApplicationContext loadContext(String... locations) throws Exception {
		String[] result = new String[locations.length + 1];
		for (int i = 0; i < locations.length; i++) {
			result[i] = locations[i];
		}
		result[locations.length] = configurationClass.getName();
		return super.loadContext(result);
	}
}