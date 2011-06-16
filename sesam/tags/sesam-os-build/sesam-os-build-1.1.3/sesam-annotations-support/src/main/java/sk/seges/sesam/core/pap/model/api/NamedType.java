package sk.seges.sesam.core.pap.model.api;

public interface NamedType extends java.lang.reflect.Type, PrintableType {

	public static final Class<?> THIS = NamedType.class;

	String getPackageName();
	
	String getSimpleName();

	String getCanonicalName();

	String getQualifiedName();
}