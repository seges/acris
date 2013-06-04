package sk.seges.sesam.core.pap.model;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.model.api.TypeParameter;

public class TypeParameterBuilder {
		public static TypeParameter get(String variable, Type... upperBounds) {
			if (upperBounds == null || upperBounds.length == 0) {
				return new TypeParameterClass(variable);
			}
			TypeBounds[] bounds = new TypeBounds[upperBounds.length];
			int i = 0;
			for (Type type: upperBounds) {
				bounds[i++] = new TypeBounds(type);
			}
			if (variable != null) {
				return new TypeParameterClass(variable, bounds);
			}
			return new TypeParameterClass(bounds);
		}

		public static TypeParameter get(String variable) {
			return get(variable, new Type[] {});
		}

		public static TypeParameter get(Type... bounds) {
			return get(null, bounds);
		}
}