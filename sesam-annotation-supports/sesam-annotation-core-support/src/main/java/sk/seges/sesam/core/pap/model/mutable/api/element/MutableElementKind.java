package sk.seges.sesam.core.pap.model.mutable.api.element;

public enum MutableElementKind {

    CONSTRUCTOR,

	EXECUTABLE {
		@Override
		public String toString() {
			return "method";
		}
	},
    ENUM_CONSTANT,
    FIELD,
    PARAMETER,
    LOCAL_VARIABLE,
    EXCEPTION_PARAMETER;

}
