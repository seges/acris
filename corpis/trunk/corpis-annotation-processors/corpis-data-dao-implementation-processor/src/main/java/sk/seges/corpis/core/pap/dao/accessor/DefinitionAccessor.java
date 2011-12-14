package sk.seges.corpis.core.pap.dao.accessor;

import sk.seges.corpis.appscaffold.shared.annotation.Definition;
import sk.seges.corpis.core.pap.dao.model.DefinitionType;
import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class DefinitionAccessor extends SingleAnnotationAccessor<Definition> {

	public DefinitionAccessor(MutableDeclaredType mutableDeclatedType, MutableProcessingEnvironment processingEnv) {
		super(mutableDeclatedType, Definition.class, processingEnv);
	}
	
	public DefinitionType getDefinition() {
		if (!isValid()) {
			return null;
		}
		//TODO: getQualifiedName()
		return new DefinitionType(processingEnv.getElementUtils().getTypeElement(annotation.value().getCanonicalName().replace("$", ".")), processingEnv);
		//return new DefinitionType(processingEnv.getElementUtils().getTypeElement(annotation.value().getCanonicalName()), processingEnv);
	}
}
