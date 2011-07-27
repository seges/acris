package sk.seges.sesam.pap.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface TransferObjectMappings {
	
	TransferObjectMapping[] value();

}