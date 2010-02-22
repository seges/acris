package sk.seges.acris.security.rebind;

import java.util.List;

import com.google.gwt.core.ext.typeinfo.HasAnnotations;

/**
 * Annotation processor is used to transform secured annotation values into
 * {@link String} representation, i.e. in some cases there are {@link Enum} used
 * in order to define client security authority. In this case
 * {@link ISecuredAnnotationProcessor} is used to convert {@link Enum} value
 * into {@link String} values.
 * 
 * @author fat
 * 
 */
public interface ISecuredAnnotationProcessor {

	List<String> getListAuthoritiesForType(HasAnnotations annotationType);

	String[] getAuthoritiesForType(HasAnnotations annotationType);

	/**
	 * Specifies if the annotation is supported security annotation
	 * 
	 * @param annotationType
	 *            concrete annotation bound with the
	 *            field/component/panel/object
	 * @return true if annotation is supported, otherwise false
	 */
	boolean isAuthorityPermission(HasAnnotations annotationType);
}
