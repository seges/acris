package sk.seges.sesam.core.pap.model.mutable.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

class MutableDeclared extends MutableType implements MutableDeclaredType {

	protected String simpleName;
	private String packageName;
	private MutableDeclaredType enclosedClass;
	private Set<AnnotationMirror> annotations = new HashSet<AnnotationMirror>();
	private List<MutableTypeVariable> typeVariables = new LinkedList<MutableTypeVariable>();

	private MutableTypeKind kind;

	private Set<MutableTypeMirror> interfaces = new HashSet<MutableTypeMirror>();
	private MutableDeclaredType superClass;

	//TypeMirror for the primitive types, otherwise DeclaredType
	private TypeMirror type;
	private boolean dirty = false;
	
	public MutableDeclared(String packageName, String simpleName) {
		this(null, packageName, simpleName);
	}

	public MutableDeclared(TypeMirror type, String packageName, String simpleName) {
		this.simpleName = simpleName;
		this.packageName = packageName;
		this.type = type;
		this.enclosedClass = null;

		if (type != null && type.getKind().equals(TypeKind.DECLARED)) {
			this.kind = convertKind(((DeclaredType)type).asElement().getKind());
		} else {
			this.kind = MutableTypeKind.CLASS;
		}
		copyAnnotations();
	}

	private MutableTypeKind convertKind(ElementKind kind) {
		switch (kind) {
		case CLASS:
			return MutableTypeKind.CLASS;
		case INTERFACE:
			return MutableTypeKind.INTERFACE;
		case ANNOTATION_TYPE:
			return MutableTypeKind.ANNOTATION_TYPE;
		case ENUM:
			return MutableTypeKind.ENUM;
		}
		
		throw new RuntimeException("Unsupported kind " + kind + ". Unable to create declared type with this kind.");
	}
	
	public MutableDeclared(TypeMirror type, MutableDeclaredType enclosedClass, String simpleName) {
		this.simpleName = simpleName;
		this.packageName = enclosedClass.getPackageName();
		this.enclosedClass = enclosedClass;
		this.type = type;
		if (type != null && type.getKind().equals(TypeKind.DECLARED)) {
			this.kind = convertKind(((DeclaredType)type).asElement().getKind());
		} else {
			this.kind = MutableTypeKind.CLASS;
		}
		copyAnnotations();
	}

	private void copyAnnotations() {
		if (type != null && type.getKind().equals(TypeKind.DECLARED)) {
			for (AnnotationMirror annotation: ((DeclaredType)type).asElement().getAnnotationMirrors()) {
				annotations.add(annotation);
			}
		}
	}

	public MutableDeclaredType getEnclosedClass() {
		return enclosedClass;
	};
	
	public MutableDeclaredType setSimpleName(String simpleName) {
		dirty();
		invalidateEnclosedType();
		this.simpleName = simpleName;
		return this;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getPackageName() {
		return packageName;
	}

	public List<MutableTypeVariable> getTypeVariables() {
		return Collections.unmodifiableList(this.typeVariables);
	};

	private void dirty() {
		type = null;
	}
	
	private void invalidateEnclosedType() {
		if (!dirty) {
			enclosedClass = null;
			dirty= true;
		}
	}
	
	public MutableDeclaredType addClassSufix(String sufix) {
		dirty();
		invalidateEnclosedType();
		simpleName += sufix;	
		return this;
	};
	
	public MutableDeclaredType addClassPrefix(String prefix) {
		dirty();
		invalidateEnclosedType();
		simpleName = prefix + simpleName;
		return this;
	};

	public MutableDeclaredType addPackageSufix(String sufix) {
		dirty();
		invalidateEnclosedType();
		packageName += sufix;
		return this;
	};

	public MutableDeclaredType changePackage(String packageName) {
		dirty();
		invalidateEnclosedType();
		this.packageName = packageName;
		return this;
	};
	
	public MutableDeclaredType changePackage(PackageValidator packageValidator) {
		return changePackage(packageValidator.toString());
	};

	public String getCanonicalName() {
		if (enclosedClass != null) {
			return enclosedClass.getCanonicalName() + "." + simpleName;
		}
		return (packageName != null ? (packageName + ".") : "") + simpleName;
	};
	
	public String getQualifiedName() {
		return getCanonicalName().replace("$", ".");
	};
		
	public TypeMirror asType() {
		return type;
	};

	public void annotateWith(AnnotationMirror annotation) {
		dirty();
		annotations.add(annotation);
	}

	@Override
	public Set<AnnotationMirror> getAnnotations() {
		return Collections.unmodifiableSet(annotations);
	};

	String toString(MutableDeclared declaredType) {
		if (declaredType.getTypeVariables() == null || declaredType.getTypeVariables().size() == 0) {
			return "";
		}
		
		String types = "<";
		
		int i = 0;
		
		for (MutableTypeVariable typeVariable: declaredType.getTypeVariables()) {
			if (i > 0) {
				types += ", ";
			}
			types += typeVariable.toString();
			i++;
		}
		
		types += ">";
		
		return types;
	}
	
	@Override
	public String toString() {
		return getCanonicalName() + toString(this);
	}

	public String getCanonicalName(boolean typed) {
		return toString(ClassSerializer.CANONICAL, typed);
	}

	public String getSimpleName(boolean typed) {
		return toString(ClassSerializer.SIMPLE, typed);
	}

	public String getQualifiedName(boolean typed) {
		return toString(ClassSerializer.QUALIFIED, typed);
	}

	public String toString(ClassSerializer serializer) {
		switch (serializer) {
		case CANONICAL:
			return getCanonicalName();
		case QUALIFIED:
			return getQualifiedName();
		case SIMPLE:
			return getSimpleName();
		}
		return null;
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		String resultName = this.toString(serializer);
		
		if (!typed || this.getTypeVariables() == null || this.getTypeVariables().size() == 0) {
			return resultName;
		}

		String types = "<";

		int i = 0;

		for (MutableTypeVariable typeParameter : this.getTypeVariables()) {
			if (i > 0) {
				types += ", ";
			}
			types += typeParameter.toString(serializer, typed);
			i++;
		}

		types += ">";

		return resultName + types;
	}
	
	public MutableTypeKind getKind() {
		return this.kind;
	};

	public MutableDeclaredType setKind(MutableTypeKind kind) {
		dirty();
		this.kind = kind;
		return this;
	}
	
	public Set<? extends MutableTypeMirror> getInterfaces() {
		return Collections.unmodifiableSet(interfaces);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MutableDeclaredType setInterfaces(Set<? extends MutableTypeMirror> interfaces) {
		dirty();
		this.interfaces = (Set<MutableTypeMirror>) interfaces;
		return this;
	}
	
	public MutableDeclaredType getSuperClass() {
		return superClass;
	}
	
	public MutableDeclaredType prefixTypeParameter(String prefix) {
		return renameTypeParameter(RenameActionType.PREFIX, prefix + "_");
	}

	public MutableDeclaredType renameTypeParameter(RenameActionType actionType, String parameter) {
		return renameTypeParameter(actionType, parameter, null, false);
	}
	
	public MutableDeclaredType renameTypeParameter(RenameActionType actionType, String parameter, String oldName, boolean recursive) {	
			
		if (getTypeVariables() == null || getTypeVariables().size() == 0) {
			return this;
		}

		dirty();
		
		for (MutableTypeVariable typeParameter: this.getTypeVariables()) {
			String variable = typeParameter.getVariable();
			
			if (variable != null && !variable.equals("?")) {
				if (oldName == null || oldName.equals(variable)) {
					variable = actionType.apply(variable, parameter);
				}
				typeParameter.setVariable(variable);
			} else if (recursive) {
				Set<? extends MutableTypeMirror> bounds = typeParameter.getUpperBounds();
				for (MutableTypeMirror upperBound: bounds) {
					if (upperBound instanceof MutableDeclaredType && ((MutableDeclaredType)upperBound).getTypeVariables().size() > 0) {
						((MutableDeclaredType)upperBound).renameTypeParameter(actionType, parameter, oldName, recursive);
					}
				}
			}
		}
	
		return this;
	}

	public MutableDeclared stripTypeParameters() {
		dirty();
		this.typeVariables = new LinkedList<MutableTypeVariable>();
		return this;
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
		result = prime * result + ((enclosedClass == null) ? 0 : enclosedClass.hashCode());
		result = prime * result + ((interfaces == null) ? 0 : interfaces.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result + ((simpleName == null) ? 0 : simpleName.hashCode());
		result = prime * result + ((superClass == null) ? 0 : superClass.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((typeVariables == null) ? 0 : typeVariables.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableDeclared other = (MutableDeclared) obj;
		if (annotations == null) {
			if (other.annotations != null)
				return false;
		} else if (!annotations.equals(other.annotations))
			return false;
		if (enclosedClass == null) {
			if (other.enclosedClass != null)
				return false;
		} else if (!enclosedClass.equals(other.enclosedClass))
			return false;
		if (interfaces == null) {
			if (other.interfaces != null)
				return false;
		} else if (!interfaces.equals(other.interfaces))
			return false;
		if (kind != other.kind)
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (simpleName == null) {
			if (other.simpleName != null)
				return false;
		} else if (!simpleName.equals(other.simpleName))
			return false;
		if (superClass == null) {
			if (other.superClass != null)
				return false;
		} else if (!superClass.equals(other.superClass))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (typeVariables == null) {
			if (other.typeVariables != null)
				return false;
		} else if (!typeVariables.equals(other.typeVariables))
			return false;
		return true;
	}

	@Override
	public MutableDeclaredType setEnclosedClass(MutableDeclaredType type) {
		dirty();
		invalidateEnclosedType();
		this.enclosedClass = type;
		return this;
	}

	@Override
	public MutableDeclaredType setTypeVariables(MutableTypeVariable... mutableTypeVariables) {
		
		dirty();
		
		this.typeVariables = new LinkedList<MutableTypeVariable>();
		
		for (MutableTypeVariable typeVariable: mutableTypeVariables) {
			this.typeVariables.add(typeVariable);
		}
		
		return this;
	}

	@Override
	public MutableDeclaredType addTypeVariable(MutableTypeVariable typeVariable) {
		dirty();
		
		this.typeVariables.add(typeVariable);
		return this;
	}

	public boolean hasTypeParameters() {
		return getTypeVariables().size() > 0;
	}

	public boolean hasVariableParameterTypes() {
		if (!hasTypeParameters()) {
			return false;
		}
		
		for (MutableTypeVariable typeParameter: getTypeVariables()) {
			if (typeParameter.getVariable() != null && typeParameter.getVariable().length() > 0) {
				return true;
			}
		}
		
		return false;
	}

	public MutableTypeVariable[] getVariableParameterTypes() {
		List<MutableTypeVariable> result = new ArrayList<MutableTypeVariable>();
		
		for (MutableTypeVariable typeParameter: getTypeVariables()) {
			if (typeParameter.getVariable() != null && typeParameter.getVariable().length() > 0) {
				result.add(typeParameter);
			}
		}
		
		return result.toArray(new MutableTypeVariable[] {});
	}

	public MutableDeclaredType stripTypeParametersTypes() {

		dirty();
		
		MutableTypeVariable[] variables = new MutableTypeVariable[getTypeVariables().size()];
		int i = 0;
		for (MutableTypeVariable typeParameter: getTypeVariables()) {
			if (typeParameter.getVariable() != null) {
				MutableVariable typeVariable = new MutableVariable();
				typeVariable.setVariable(typeParameter.getVariable().toString());
				variables[i] = typeVariable;
			} else {
				variables[i] = typeParameter;
			}
			i++;
		}

		return setTypeVariables(variables);
	}

	@Override
	public MutableDeclaredType setSuperClass(MutableDeclaredType superClass) {
		dirty();
		this.superClass = superClass;

		if (superClass.hasVariableParameterTypes() && !hasTypeParameters()) {
			setTypeVariables(superClass.getVariableParameterTypes());
			superClass.stripTypeParametersTypes();
		}

		return this;
	}
	
	@Override
	public MutableDeclaredType clone() {
		MutableDeclared result = new MutableDeclared(type, packageName, simpleName);
		result.enclosedClass = enclosedClass;
		
		for (AnnotationMirror annotationMirror: annotations) {
			result.annotations.add(annotationMirror);
		}
		
		for (MutableTypeVariable typeVariable: typeVariables) {
			result.typeVariables.add(typeVariable.clone());
		}
		
		result.dirty = dirty;
		result.kind = kind;
		
		for (MutableTypeMirror interfaceType: interfaces) {
			result.interfaces.add(interfaceType);
		}
		
		result.superClass = superClass;

		return result;
	}
}