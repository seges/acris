package sk.seges.sesam.core.pap.model;

import java.util.Arrays;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.NamedType;
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

	public String toString(NamedType inputClass, ClassSerializer serializer) {
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
				if (typeVariable.getUpperBound().equals(NamedType.THIS)) {
					result += inputClass.toString(ClassSerializer.SIMPLE);
				} else {
					result += ClassUtils.toString(inputClass, typeVariable.getUpperBound(), serializer, true);
				}
				i++;
			}
		}

		if (result.length() == 0) {
			throw new IllegalArgumentException("Invalid type parameter");
		}
		return result;
	}

	public String toString() {
		String result = "";

		if (getVariable() != null) {
			result += getVariable() + " ";
		}

		if (getBounds() != null) {
			if (getVariable() != null) {
				result += "extends ";
			}

			int i = 0;
			for (TypeVariable typeVariable : getBounds()) {
				if (i > 0) {
					result += " & ";
				}

				if (typeVariable.getUpperBound().equals(NamedType.THIS)) {
					result += "_THIS_";
				} else {
					if (typeVariable.getUpperBound() instanceof Class) {
						result += ((Class<?>) typeVariable.getUpperBound()).getCanonicalName();
					} else {
						result += getBounds().toString();
					}
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
}