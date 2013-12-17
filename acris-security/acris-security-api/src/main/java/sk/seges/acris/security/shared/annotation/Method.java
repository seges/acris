package sk.seges.acris.security.shared.annotation;

import sk.seges.acris.security.shared.MethodType;

public @interface Method {

	MethodType value() default MethodType.POST;
}
