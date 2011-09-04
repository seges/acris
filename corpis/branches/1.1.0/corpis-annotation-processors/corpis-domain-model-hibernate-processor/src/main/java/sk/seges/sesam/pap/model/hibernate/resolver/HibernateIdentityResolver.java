package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.resolver.api.IdentityResolver;

public class HibernateIdentityResolver implements IdentityResolver {

	@Override
	public boolean shouldHaveIdMethod(ConfigurationTypeElement configurationElement, TypeMirror domainType) {
		if (!domainType.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		TypeElement domainElement = (TypeElement)((DeclaredType)domainType).asElement();
		if (domainElement.getAnnotation(Entity.class) != null) {
			return domainElement.getAnnotation(Embeddable.class) == null;
		}
		return false;
	}
}
