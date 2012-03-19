package sk.seges.sesam.core.pap.model.mutable.api;

import java.util.Set;

public interface MutableTypeVariable extends MutableTypeMirror {

	String getVariable();
	MutableTypeVariable setVariable(String variable);

	Set<? extends MutableTypeMirror> getLowerBounds();
	MutableTypeVariable setLowerBounds(Set<? extends MutableTypeMirror> bounds);
	MutableTypeVariable addLowerBound(MutableTypeMirror bound);

	Set<? extends MutableTypeMirror> getUpperBounds();
	MutableTypeVariable setUpperBounds(Set<? extends MutableTypeMirror> bounds);
	MutableTypeVariable addUpperBound(MutableTypeMirror bound);

	MutableTypeVariable clone();
}