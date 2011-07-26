package sk.seges.sesam.pap.model.model;

import javax.lang.model.element.TypeElement;


public interface ElementPrinter {
	
	void initialize(TypeElement typeElement);
	
	void print(ProcessorContext context);
	
	void finish(TypeElement typeElement);
}