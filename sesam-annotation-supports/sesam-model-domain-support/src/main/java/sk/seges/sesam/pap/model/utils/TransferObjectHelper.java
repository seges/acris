package sk.seges.sesam.pap.model.utils;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.annotation.Field;

public class TransferObjectHelper {

	public static final String DTO_SUFFIX = "Dto";
	public static final String DEFAULT_SUFFIX = "Configuration";

	private TransferObjectHelper() {};
	
	//TODO Move to the configuration
	public static String getFieldPath(ExecutableElement method) {
		if (method == null) {
			return null;
		}

		Field fieldAnnotation = method.getAnnotation(Field.class);

		String fieldPath = MethodHelper.toField(method);

		if (fieldAnnotation != null && NullCheck.checkNull(fieldAnnotation.value()) != null) {
			fieldPath = fieldAnnotation.value();
		}

		return fieldPath;
	}	
}