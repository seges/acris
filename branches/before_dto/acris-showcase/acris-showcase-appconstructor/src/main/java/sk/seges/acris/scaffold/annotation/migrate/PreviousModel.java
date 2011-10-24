package sk.seges.acris.scaffold.annotation.migrate;

public @interface PreviousModel {
	String version();
	Class<?> currentModel();
}
