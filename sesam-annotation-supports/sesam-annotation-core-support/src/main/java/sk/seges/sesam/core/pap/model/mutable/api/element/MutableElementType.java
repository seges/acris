package sk.seges.sesam.core.pap.model.mutable.api.element;

import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.api.HasAnnotations;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public interface MutableElementType extends HasAnnotations {

	MutableTypeMirror asType();

	MutableElementKind getKind();

	Set<Modifier> getModifiers();
	<T extends MutableElementType> T addModifier(Modifier... modifiers);

	String getSimpleName();

	MutableElementType getEnclosingElement();

	List<MutableElementType> getEnclosedElements();
}
