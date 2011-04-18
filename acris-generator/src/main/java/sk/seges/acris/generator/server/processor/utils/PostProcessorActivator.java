package sk.seges.acris.generator.server.processor.utils;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public class PostProcessorActivator {
	
	private String[] inactiveProcessors;
	private String[] inactiveIndexProcessors;
	
	private static final String PROCESSOR_SEPARATOR = ",";
	
	public PostProcessorActivator(String inactiveProcessors, String inactiveIndexProcessors) {
		this.inactiveProcessors = parseProcessors(inactiveProcessors);
		this.inactiveIndexProcessors = parseProcessors(inactiveIndexProcessors);
	}

	private String[] parseProcessors(String inactiveProcessors) {
		String[] result;
		
		if (inactiveProcessors == null || inactiveProcessors.length() == 0) {
			result = new String[0];
		} else {
			result = inactiveProcessors.split(PROCESSOR_SEPARATOR);
			if (result == null) {
				result = new String[0];
			}
		}
		for (int i = 0; i < result.length; i++) {
			if (result[i] == null) {
				result[i] = "";
			} else {
				result[i] = result[i].trim();
			}
		}
		
		return result;
	}
	
	public boolean isActive(AbstractElementPostProcessor postProcessor, boolean index) {
		if (index) {
			for (int i = 0; i < this.inactiveIndexProcessors.length; i++) {
				if (this.inactiveIndexProcessors[i].equals(postProcessor.getClass().getSimpleName())) {
					return false;
				}
			}
		} else {
			for (int i = 0; i < this.inactiveProcessors.length; i++) {
				if (this.inactiveProcessors[i].equals(postProcessor.getClass().getSimpleName())) {
					return false;
				}
			}
		}
		return true;
	}
}