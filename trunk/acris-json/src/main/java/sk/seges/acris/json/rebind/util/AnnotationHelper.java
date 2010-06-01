package sk.seges.acris.json.rebind.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHelper {

	public static Map<String, String> getMembers(Annotation annotation) {
		Map<String, String> params = new HashMap<String, String>();

		for (Method method : annotation.annotationType().getDeclaredMethods()) {
			try {
				Object obj = method.invoke(annotation);
				params.put(annotation.annotationType().getName() + "_" + method.getName(), obj.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return params;
	}
}