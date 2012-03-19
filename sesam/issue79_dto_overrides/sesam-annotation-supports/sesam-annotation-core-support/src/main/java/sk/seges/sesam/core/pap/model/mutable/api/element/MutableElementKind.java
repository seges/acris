package sk.seges.sesam.core.pap.model.mutable.api.element;

public enum MutableElementKind {

	EXECUTABLE {
		@Override
		public String toString() {
			return "method";
		}
	};

}
