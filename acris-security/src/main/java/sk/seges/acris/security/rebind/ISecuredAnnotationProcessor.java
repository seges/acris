package sk.seges.acris.security.rebind;

import java.util.List;

import com.google.gwt.core.ext.typeinfo.HasAnnotations;

public interface ISecuredAnnotationProcessor {

	List<String> getListAuthoritiesForType(HasAnnotations annotationType);

	String[] getAuthoritiesForType(HasAnnotations annotationType);
	
	boolean isAuthorityPermission(HasAnnotations annotationType);
}
