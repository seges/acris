package sk.seges.acris.pap.security.provider;

import java.util.List;

import javax.lang.model.element.Element;

/**
 * Secured authorities provider is used to transform secured annotation values into
 * {@link String} representation, i.e. in some cases there are {@link Enum} used
 * in order to define client security authority. In this case
 * {@link SecuredAuthoritiesProvider} is used to convert {@link Enum} value
 * into {@link String} values.
 * 
 * @author fat
 * 
 */
public interface SecuredAuthoritiesProvider {

	List<String> getListAuthoritiesForType(Element annotationType);

	String[] getAuthoritiesForType(Element annotationType);

}