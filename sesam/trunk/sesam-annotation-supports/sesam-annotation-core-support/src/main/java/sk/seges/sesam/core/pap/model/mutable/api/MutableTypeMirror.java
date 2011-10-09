package sk.seges.sesam.core.pap.model.mutable.api;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;

public interface MutableTypeMirror extends Type {

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
		
	String toString(ClassSerializer serializer);
	String toString(ClassSerializer serializer, boolean typed);
}
