package sk.seges.acris.generator.server.processor.utils;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public class PostProcessorActivator {
	
	private String[] inactiveProcessors;
	
	private static final String PROCESSOR_SEPARATOR = ",";
	
	public PostProcessorActivator(String inactiveProcessors) {
		if (inactiveProcessors == null || inactiveProcessors.length() == 0) {
			this.inactiveProcessors = new String[0];
		} else {
			this.inactiveProcessors = inactiveProcessors.split(PROCESSOR_SEPARATOR);
			if (this.inactiveProcessors == null) {
				this.inactiveProcessors = new String[0];
			}
		}
		for (int i = 0; i < this.inactiveProcessors.length; i++) {
			if (this.inactiveProcessors[i] == null) {
				this.inactiveProcessors[i] = "";
			} else {
				this.inactiveProcessors[i] = this.inactiveProcessors[i].trim();
			}
		}
	}
	
	public boolean isActive(AbstractElementPostProcessor postProcessor) {
		for (int i = 0; i < this.inactiveProcessors.length; i++) {
			if (this.inactiveProcessors[i].equals(postProcessor.getClass().getSimpleName())) {
				return false;
			}
		}
		
		return true;
	}
}