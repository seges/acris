package sk.seges.sesam.core.pap.test.cases.model;

import sk.seges.sesam.core.pap.test.cases.annotation.BasicTestAnnotation;

@BasicTestAnnotation
public interface BasicModel {

	int numberField();
	
	String textField();
	
	ReferenceModel reference();
	AnotherModel another();
}
