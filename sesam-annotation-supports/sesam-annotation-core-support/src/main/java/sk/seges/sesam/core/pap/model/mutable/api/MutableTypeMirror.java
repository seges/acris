package sk.seges.sesam.core.pap.model.mutable.api;

import java.lang.reflect.Type;

public interface MutableTypeMirror extends Type, MutableType {

	public enum MutableTypeKind {
		ENUM {
			@Override
			public String toString() {
				return "enum";
			}
		},
		CLASS {
			@Override
			public String toString() {
				return "class";
			}
		},
		ANNOTATION_TYPE {
			@Override
			public String toString() {
				return "@interface";
			}
		},
		INTERFACE{
			@Override
			public String toString() {
				return "interface";
			}
		},
		PRIMITIVE,
		WILDCARD,
		TYPEVAR,
		ARRAY,
		VOID{
			@Override
			public String toString() {
				return "void";
			}
		};

		public boolean isDeclared() {
			return this.equals(MutableTypeKind.CLASS) || this.equals(MutableTypeKind.INTERFACE) || this.equals(MutableTypeKind.ANNOTATION_TYPE) || this.equals(MutableTypeKind.ENUM);
		}		
	}
	
	MutableTypeKind getKind();
		
}
