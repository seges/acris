package sk.seges.acris.security.shared.session;


public enum ValueType {
	BOOLEAN {
		@Override
		public Object getValue(PropertyHolder propertyHolder) {
			return propertyHolder.getBooleanValue();
		}

		@Override
		public ValueType setValue(PropertyHolder propertyHolder, Object value) {
			propertyHolder.setBooleanValue((Boolean) value);
			return ValueType.BOOLEAN;
		}

		@Override
		public Class<?> appliesFor() {
			return Boolean.class;
		}
	},
	STRING {
		@Override
		public Object getValue(PropertyHolder propertyHolder) {
			return propertyHolder.getStringValue();
		}

		@Override
		public ValueType setValue(PropertyHolder propertyHolder, Object value) {
			propertyHolder.setStringValue((String) value);
			return ValueType.STRING;
		}

		@Override
		public Class<?> appliesFor() {
			return String.class;
		}
	},
	ARRAY {
		@Override
		public Object getValue(PropertyHolder propertyHolder) {
			return propertyHolder.getArrayValue();
		}

		@Override
		public ValueType setValue(PropertyHolder propertyHolder, Object value) {
			propertyHolder.setArrayValue((SessionArrayHolder) value);
			return ValueType.ARRAY;
		}

		@Override
		public Class<?> appliesFor() {
			return SessionArrayHolder.class;
		}
	},
	ENUM {
		@Override
		public Object getValue(PropertyHolder propertyHolder) {
			return propertyHolder.getEnumValue();
		}

		@Override
		public ValueType setValue(PropertyHolder propertyHolder, Object value) {
			propertyHolder.setEnumValue((Enum<?>) value);
			return ValueType.ENUM;
		}

		@Override
		public Class<?> appliesFor() {
			return Enum.class;
		}
	};

	public abstract Object getValue(PropertyHolder propertyHolder);
	public abstract ValueType setValue(PropertyHolder propertyHolder, Object value);
	public abstract Class<?> appliesFor();

	public static ValueType valueFor(Object obj) {
		if (obj == null) {
			return ValueType.STRING;
		}

		for (ValueType valueType: ValueType.values()) {
			if (valueType.appliesFor().equals(obj.getClass())) {
				return valueType;
			}
		}

		if (obj.getClass().isEnum()) {
			return ValueType.ENUM;
		}

		throw new RuntimeException("Not supported class " + obj.getClass().getName());
	}
}