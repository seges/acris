package sk.seges.sesam.pap.model.annotation;

import javax.lang.model.element.ElementKind;

public enum PropertyAccessor {
	FIELD {
		@Override
		public boolean supportsKind(ElementKind kind) {
			return kind.equals(ElementKind.FIELD);
		}
	}, 
	
	PROPERTY {
		@Override
		public boolean supportsKind(ElementKind kind) {
			return kind.equals(ElementKind.METHOD);
		}
	};
	
	public abstract boolean supportsKind(ElementKind kind);
}
