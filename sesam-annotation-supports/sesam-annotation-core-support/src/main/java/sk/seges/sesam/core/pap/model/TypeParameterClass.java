package sk.seges.sesam.core.pap.model;

import java.util.Arrays;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.model.api.TypeVariable;
import sk.seges.sesam.core.pap.utils.ClassUtils;

class TypeParameterClass implements TypeParameter {

	private String variable;
	private TypeVariable[] bounds;

	public TypeParameterClass(String variable) {
		this.variable = variable;
	}

	public TypeParameterClass(TypeVariable... bounds) {
		this.bounds = bounds;
	}

	public TypeParameterClass(String variable, TypeVariable... bounds) {
		this(variable);
		this.bounds = bounds;
	}

	@Override
	public String getVariable() {
		return variable;
	}

	@Override
	public TypeVariable[] getBounds() {
		return bounds;
	}

	public String toString(ClassSerializer serializer) {
		return toString(serializer, true);
	}

	public String toString() {
		String result = "";

		if (getVariable() != null) {
			result += getVariable();
		}

		if (getBounds() != null) {
			
			if (getVariable() != null) {
				result += " extends ";
			}

			int i = 0;
			for (TypeVariable typeVariable : getBounds()) {
				if (i > 0) {
					result += " & ";
				}

				if (typeVariable.getUpperBound() instanceof Class) {
					result += ((Class<?>) typeVariable.getUpperBound()).getCanonicalName();
				} else {
					result += getBounds().toString();
				}
				i++;
			}
		}

		return result;
	}

	public String toString(ClassSerializer serializer, boolean typed) {
		String result = "";

		if (getVariable() != null) {
			result += getVariable();
		}

		if (getBounds() != null) {
			if (getVariable() != null) {
				result += " extends ";
			}

			int i = 0;
			
			for (TypeVariable typeVariable : getBounds()) {
				if (i > 0) {
					result += " & ";
				}

				if (typeVariable.getUpperBound() instanceof Class) {
					result += ClassUtils.toString((Class<?>)typeVariable.getUpperBound(), serializer);
				} else {
					result += ClassUtils.toString(typeVariable.getUpperBound(), serializer, true);
				}
				i++;
			}
		}

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bounds);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeParameterClass other = (TypeParameterClass) obj;
		if (!Arrays.equals(bounds, other.bounds))
			return false;
		return true;
	}

	@Override
	public String getPackageName() {
		return "";
	}

	@Override
	public String getSimpleName() {
		return toString(ClassSerializer.SIMPLE);
	}

	@Override
	public String getCanonicalName() {
		return toString(ClassSerializer.CANONICAL);
	}

	@Override
	public String getQualifiedName() {
		return toString(ClassSerializer.QUALIFIED);
	}

	@Override
	public TypeMirror asType() {
		return null;
	}

	@Override
	public void annotateWith(AnnotationMirror annotationMirror) {
		throw new RuntimeException("Unsupported operation. TypeParameter cannot be annotated with " + annotationMirror.toString());
	}

	@Override
	public Set<AnnotationMirror> getAnnotations() {
		throw new RuntimeException("Unsupported operation. TypeParameter cannot be annotated.");
	}
}