package sk.seges.sesam.core.pap.model.mutable.api;

import java.util.List;
import java.util.Set;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.HasAnnotations;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

public interface MutableDeclaredType extends MutableTypeMirror, HasAnnotations {

	public enum RenameActionType {
		PREFIX {
			@Override
			public String apply(String name, String parameter) {
				return parameter + name;
			}
		}, 
		REPLACE {
			@Override
			public String apply(String name, String parameter) {
				return parameter;
			}
		}, 
		SUFIX {
			@Override
			public String apply(String name, String parameter) {
				return name + parameter;
			}
		};
		
		public abstract String apply(String name, String parameter);
	}

	MutableDeclaredType getEnclosedClass();
	MutableDeclaredType setEnclosedClass(MutableDeclaredType type);

	MutableDeclaredType setSimpleName(String simpleName);
	String getSimpleName();

	String getPackageName();

	List<? extends MutableTypeVariable> getTypeVariables();
	MutableDeclaredType setTypeVariables(MutableTypeVariable... mutableTypeVariables);
	MutableDeclaredType addTypeVariable(MutableTypeVariable typeVariable);

	MutableDeclaredType addClassSufix(String sufix);
	MutableDeclaredType addClassPrefix(String prefix);
	
	MutableDeclaredType replaceClassSuffix(String oldSuffix, String newSuffix);
	MutableDeclaredType replaceClassPrefix(String oldPrefix, String newPrefix);

	MutableDeclaredType removeClassSuffix(String originalSuffix);
	MutableDeclaredType removeClassPrefix(String originalPrefix);

	MutableDeclaredType addPackageSufix(String sufix);

	MutableDeclaredType changePackage(String packageName);
	MutableDeclaredType changePackage(PackageValidator packageValidator);

	String getCanonicalName();
	String getQualifiedName();

	TypeMirror asType();

	String toString(ClassSerializer serializer);
	String toString(ClassSerializer serializer, boolean typed);

	MutableDeclaredType setKind(MutableTypeKind kind);

	Set<? extends MutableTypeMirror> getInterfaces();
	MutableDeclaredType setInterfaces(Set<? extends MutableTypeMirror> interfaces);

	MutableDeclaredType getSuperClass();
	MutableDeclaredType setSuperClass(MutableDeclaredType superClass);

	MutableDeclaredType stripTypeParameters();
	MutableDeclaredType stripTypeParametersTypes();
	MutableDeclaredType prefixTypeParameter(String prefix);
	MutableDeclaredType renameTypeParameter(RenameActionType actionType, String parameter);
	MutableDeclaredType renameTypeParameter(RenameActionType actionType, String parameter, String oldName, boolean recursive);

	boolean hasTypeParameters();
	boolean hasVariableParameterTypes();
	MutableTypeVariable[] getVariableParameterTypes();

	MutableDeclaredType clone();
}