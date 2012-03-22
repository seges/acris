package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.model.mutable.api.reference.ExecutableElementReference;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;

public class MutableTypes implements Types {

	private final Types types;
	private final Elements elements;
	private final MutableProcessingEnvironment processingEnv;
	
	public MutableTypes(MutableProcessingEnvironment processingEnv, Elements elements, Types types) {
		this.types = types;
		this.elements = elements;
		this.processingEnv = processingEnv;
	}

	@Override
	public Element asElement(TypeMirror t) {
		return this.types.asElement(t);
	}

	@Override
	public boolean isSameType(TypeMirror t1, TypeMirror t2) {
		return this.types.isSameType(t1, t2);
	}

	public boolean isSameType(MutableTypeMirror type1, MutableTypeMirror type2) {
		if (type1 == null && type2 == null) {
			return true;
		}
		
		if (type1 == null || type2 == null) {
			return false;
		}
		
		if (!type1.getKind().equals(type2.getKind())) {
			return false;
		}
		
		if (type1 instanceof MutableDeclaredType) {
			return ((MutableDeclaredType)type1).getCanonicalName().equals(((MutableDeclaredType)type2).getCanonicalName());
		}
		
		if (type1 instanceof MutableTypeVariable) {
			MutableTypeVariable dtoVariable1 = (MutableTypeVariable)type1;
			MutableTypeVariable dtoVariable2 = (MutableTypeVariable)type2;
			
			if (dtoVariable1.getVariable() != null && dtoVariable2.getVariable() != null && !dtoVariable1.getVariable().equals(dtoVariable2.getVariable())) {
				return false;
			}
			
			if ((dtoVariable1.getVariable() == null && dtoVariable2.getVariable() != null) ||
				(dtoVariable2.getVariable() == null && dtoVariable1.getVariable() != null)) {
				return false;
			}

			if (!areSameTypes(dtoVariable1.getUpperBounds(), dtoVariable2.getUpperBounds())) {
				return false;
			}

			if (!areSameTypes(dtoVariable1.getLowerBounds(), dtoVariable2.getLowerBounds())) {
				return false;
			}
			
			return true;
		}
		
		if (type1 instanceof MutableArrayType) {
			return isSameType(((MutableArrayType)type1).getComponentType(), ((MutableArrayType)type2).getComponentType());
		}
		
		return false;
	}

	@Override
	public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
		return this.types.isSubtype(t1, t2);
	}

	@Override
	public boolean isAssignable(TypeMirror t1, TypeMirror t2) {
		return this.types.isAssignable(t1, t2);
	}

	public boolean isAssignable(MutableTypeMirror t1, MutableTypeMirror t2) {
		if (isSameType(t1, t2)) {
			return true;
		}

		if (implementsType(t1, t2)) {
			return true;
		}

		if (t1.getKind().isDeclared()) {
			if (((MutableDeclaredType)t1).getSuperClass() == null) {
				return false;
			}
			return isAssignable(((MutableDeclaredType)t1).getSuperClass(), t2);
		}
		
		return false;
	}

	@Override
	public boolean contains(TypeMirror t1, TypeMirror t2) {
		return this.types.contains(t1, t2);
	}

	@Override
	public boolean isSubsignature(ExecutableType m1, ExecutableType m2) {
		return this.types.isSubsignature(m1, m2);
	}

	@Override
	public List<? extends TypeMirror> directSupertypes(TypeMirror t) {
		
		List<TypeMirror> superTypes = new ArrayList<TypeMirror>();
		
		if (t.getKind().equals(TypeKind.DECLARED)) {
			DeclaredType declaredType = ((DeclaredType)t);
			
			if (declaredType.asElement().getKind().equals(ElementKind.CLASS) ||
				declaredType.asElement().getKind().equals(ElementKind.INTERFACE)) {
				TypeElement typeElement = (TypeElement)declaredType.asElement();
				
				if ("".equals(typeElement.getSimpleName().toString().trim())) {
					if (typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
						superTypes.add(typeElement.getSuperclass());
					}
					
					for (TypeMirror typeInterface: typeElement.getInterfaces()) {
						if (typeInterface.getKind().equals(TypeKind.DECLARED)) {
							superTypes.add(typeInterface);
						}
					}
				} else {
					superTypes.add(t);
				}
			} else {
				superTypes.add(t);
			}
		} else {
			superTypes.add(t);
		}
	
		return superTypes;
//		return this.types.directSupertypes(t);
	}

	public MutableReferenceType getReference(MutableTypeValue type, String name, boolean inline) {
		return new MutableReference(type, name, inline);
	}
	
	public MutableReferenceType getReference(MutableTypeValue type, String name) {
		return getReference(type, name, false);
	}
	
	@Override
	public TypeMirror erasure(TypeMirror t) {
		return this.types.erasure(t);
	}

	@Override
	public TypeElement boxedClass(PrimitiveType p) {
		return this.types.boxedClass(p);
	}

	@Override
	public PrimitiveType unboxedType(TypeMirror t) {
		return this.types.unboxedType(t);
	}

	@Override
	public TypeMirror capture(TypeMirror t) {
		return this.types.capture(t);
	}

	@Override
	public PrimitiveType getPrimitiveType(TypeKind kind) {
		return this.types.getPrimitiveType(kind);
	}

	@Override
	public NullType getNullType() {
		return this.types.getNullType();
	}

	@Override
	public NoType getNoType(TypeKind kind) {
		return this.types.getNoType(kind);
	}

	@Override
	public ArrayType getArrayType(TypeMirror componentType) {
		return this.types.getArrayType(componentType);
	}

	public MutableArrayType getArrayType(MutableTypeMirror componentType) {
		return new MutableArray(componentType);
	}

	@Override
	public WildcardType getWildcardType(TypeMirror extendsBound, TypeMirror superBound) {
		return this.types.getWildcardType(extendsBound, superBound);
	}

	public MutableWildcardType getWildcardType(MutableTypeMirror extendsBound, MutableTypeMirror superBound) {
		MutableWildcard mutableWildcard = new MutableWildcard();
		mutableWildcard.setExtendsBound(extendsBound);
		mutableWildcard.setSuperBound(superBound);
		return mutableWildcard;
	}

	public MutableTypeVariable getTypeVariable(String name, MutableTypeMirror[] upperBounds, MutableTypeMirror[] lowerBounds) {
		MutableVariable typeVariable = new MutableVariable();
		typeVariable.setVariable(name);
		Set<MutableTypeMirror> bounds = new HashSet<MutableTypeMirror>();
		for (MutableTypeMirror bound: upperBounds) {
			if (bound != null && (!bound.getKind().isDeclared() || !bound.toString().equals(Object.class.getCanonicalName()))) {
				bounds.add(bound);
			}
		}
		typeVariable.setUpperBounds(bounds);

		bounds = new HashSet<MutableTypeMirror>();
		for (MutableTypeMirror bound: lowerBounds) {
			if (bound != null && (!bound.getKind().isDeclared() || !bound.toString().equals(Object.class.getCanonicalName()))) {
				bounds.add(bound);
			}
		}
		typeVariable.setLowerBounds(bounds);

		return typeVariable;
	}
	
	public MutableTypeVariable getTypeVariable(String name, MutableTypeMirror... upperBounds) {
		MutableVariable typeVariable = new MutableVariable();
		typeVariable.setVariable(name);
		Set<MutableTypeMirror> bounds = new HashSet<MutableTypeMirror>();
		for (MutableTypeMirror bound: upperBounds) {
			if (bound != null) {
				if (bound != null && (!bound.getKind().isDeclared() || name == null || !bound.toString().equals(Object.class.getCanonicalName()))) {
					bounds.add(bound);
				}
			}
		}
		typeVariable.setUpperBounds(bounds);
		return typeVariable;
	}
	
	@Override
	public DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs) {
		return this.types.getDeclaredType(typeElem, typeArgs);
	}

	MutableDeclaredType constructDeclaredType(MutableDeclaredType type, MutableTypeVariable... typeArgs) {
		if (type.getEnclosedClass() != null) {
			return new MutableDeclared(type.asType(), type.getEnclosedClass(), type.getSimpleName(), processingEnv).setTypeVariables(typeArgs);
		}
		return new MutableDeclared(type.asType(), type.getPackageName(), type.getSimpleName(), processingEnv).setTypeVariables(typeArgs);
	}

	public MutableDeclaredType getDeclaredType(MutableDeclaredType type, MutableTypeVariable... typeArgs) {
		return constructDeclaredType(type, typeArgs);
	}

	public MutableDeclaredType getDeclaredType(MutableDeclaredType type, MutableDeclaredType... typeArgs) {
		MutableTypeVariable[] typeVariables = new MutableTypeVariable[typeArgs.length];
		for (int i = 0; i < typeArgs.length; i++) {
			typeVariables[i] = getTypeVariable(null, typeArgs[i]);
		}
		return getDeclaredType(type, typeVariables);
	}

	public MutableDeclaredType getDeclaredType(MutableDeclaredType enclosedType, MutableDeclaredType type, MutableTypeVariable... typeArgs) {
		return new MutableDeclared(type.asType(), enclosedType, type.getSimpleName(), processingEnv).setTypeVariables(typeArgs);
	}

	@Override
	public DeclaredType getDeclaredType(DeclaredType containing, TypeElement typeElem, TypeMirror... typeArgs) {
		return this.types.getDeclaredType(containing, typeElem, typeArgs);
	}

	@Override
	public TypeMirror asMemberOf(DeclaredType containing, Element element) {
		return this.types.asMemberOf(containing, element);
	}

	private MutableTypeMirror[] toMutableTypes(Collection<? extends TypeMirror> types) {
		if (types.size() == 0) {
			return new MutableTypeMirror[] {};
		}
		
		List<MutableTypeMirror> result = new LinkedList<MutableTypeMirror>();

		Iterator<? extends TypeMirror> iterator = types.iterator();
		
		while (iterator.hasNext()) {
			TypeMirror type = iterator.next();
			MutableTypeMirror mutableType = toMutableType(type);
			if (mutableType != null) {
				result.add(mutableType);
			}
		}
		
		return result.toArray(new MutableTypeMirror[] {});
	}
	
	public MutableDeclaredType toMutableType(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		if (clazz.getEnclosingClass() != null) {
			return new MutableDeclared(null, toMutableType(clazz.getEnclosingClass()), clazz.getSimpleName(), processingEnv);
		}
		
		//TODO handle type variable also, interfaces etc
		TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
		TypeMirror type = null;
		
		if (typeElement != null) {
			type = typeElement.asType();
		}

		if (clazz.getPackage() == null) {
			return new MutableDeclared(type, (String)null, clazz.getSimpleName(), processingEnv);
		}
		
		return new MutableDeclared(type, clazz.getPackage().getName(), clazz.getSimpleName(), processingEnv);
	}

	public MutableTypeMirror toMutableType(Type javaType) {
		if (javaType instanceof Class) {
			return toMutableType((Class<?>)javaType);
		}
		
		if (javaType instanceof MutableTypeMirror) {
			return (MutableTypeMirror)javaType;
		}

		return null;
	}

	public MutableDeclaredType toMutableType(DeclaredType declaredType) {
		return (MutableDeclaredType) convertToMutableType(declaredType);
	}
		
	public MutableDeclaredType toMutableType(TypeElement typeElement) {
		return (MutableDeclaredType) toMutableType(typeElement.asType());
	}
	
	private static Map<TypeMirror, MutableTypeMirror> typesCache = new HashMap<TypeMirror, MutableTypeMirror>();

	void invalidateCache(TypeMirror type) {
		typesCache.remove(type);
	}
	
	private <T extends MutableTypeMirror> T addToCache(T mutableType, TypeMirror typeMirror) {
		typesCache.put(typeMirror, mutableType);
		return mutableType;
	}
	
	public MutableTypeMirror toMutableType(TypeMirror typeMirror) {
		
		if (typeMirror == null || typeMirror.getKind().equals(TypeKind.NULL)  || typeMirror.getKind().equals(TypeKind.NONE)) {
			return null;
		}
		
		switch (typeMirror.getKind()) {
		case ARRAY:
			return addToCache(new MutableArray(toMutableType(((ArrayType)typeMirror).getComponentType())), typeMirror);
		case DECLARED:
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case VOID:
			return convertToMutableType(typeMirror);
		case WILDCARD:
			WildcardType wildcardType = (WildcardType)typeMirror;

			return getWildcardType(toMutableType(wildcardType.getExtendsBound()), toMutableType(wildcardType.getSuperBound()));

		case TYPEVAR:
			TypeVariable typeVariable = ((TypeVariable)typeMirror);
			
			String name = typeVariable.asElement().getSimpleName().toString();
	
			List<? extends TypeMirror> upperBounds = directSupertypes(typeVariable.getUpperBound());
			List<? extends TypeMirror> lowerBounds = directSupertypes(typeVariable.getLowerBound());
			
			return getTypeVariable(name, toMutableTypes(upperBounds), toMutableTypes(lowerBounds));
		case NULL:
		case NONE:
			return null;
		}
		
		throw new RuntimeException("Unsupported type " + typeMirror.getKind());
	}
	
	private MutableDeclaredType convertToMutableType(TypeMirror typeMirror) {
		
		MutableTypeMirror cachedType = typesCache.get(typeMirror);
		
		if (cachedType != null) {
			return (MutableDeclaredType) cachedType;
		}

		switch (typeMirror.getKind()) {
		case DECLARED:
			DeclaredType declaredType = (DeclaredType)typeMirror;
			
			if (declaredType.asElement().getEnclosingElement() != null && declaredType.asElement().getEnclosingElement().asType().getKind().equals(TypeKind.DECLARED)) {
				MutableDeclaredType enclosedType = convertToMutableType(declaredType.asElement().getEnclosingElement().asType());
				return addToCache(new MutableDeclared(declaredType, enclosedType, declaredType.asElement().getSimpleName().toString(), processingEnv), typeMirror);
			}
				
			PackageElement packageElement = elements.getPackageOf(declaredType.asElement());
			return addToCache(new MutableDeclared(declaredType, packageElement.getQualifiedName().toString(), declaredType.asElement().getSimpleName().toString(), processingEnv), typeMirror);
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case VOID:
			return addToCache(new MutableDeclared(typeMirror, (String)null, typeMirror.getKind().name().toLowerCase(), processingEnv), typeMirror);
		}
		
		throw new RuntimeException("Unsupported type " + typeMirror.getKind());
	}

	public TypeMirror fromMutableType(MutableTypeMirror type) {
	
		if (type == null) {
			return null;
		}
		
		if (type instanceof DelegateMutableDeclaredType) {
			return fromMutableType(((DelegateMutableDeclaredType)type).ensureDelegateType());
		}

		if (type instanceof MutableArrayType) {
			return types.getArrayType(fromMutableType(((MutableArrayType)type).getComponentType()));
		}

		if (type instanceof MutableWildcardType) {
			return getWildcardType(
					fromMutableType(((MutableWildcardType)type).getExtendsBound()),
					fromMutableType(((MutableWildcardType)type).getSuperBound()));
		}

		if (type instanceof MutableTypeVariable) {
			MutableTypeVariable typeParameter = (MutableTypeVariable)type;
			
			if (typeParameter.getVariable() == null || typeParameter.getVariable().length() == 0) {
				//no variable
				if (typeParameter.getUpperBounds().size() > 0) {
					if (typeParameter.getUpperBounds().size() == 1) {
						return fromMutableType(typeParameter.getUpperBounds().iterator().next());
					} else {
						//TODO what if there is no variable and more bounds ?!, like List<Serializable & Comparable>
					}
				} else if (typeParameter.getLowerBounds().size() > 0) {
					if (typeParameter.getLowerBounds().size() == 1) {
						return fromMutableType(typeParameter.getLowerBounds().iterator().next());
					} else {
						//TODO what if there is no variable and more bounds ?!, like List<Serializable & Comparable>
					}
				} else {
					return elements.getTypeElement(Object.class.getCanonicalName()).asType();
				}
			}
			
			return null;
		}
				
		if (type instanceof MutableDeclaredType) {
			MutableDeclaredType mutableDeclaredType = (MutableDeclaredType)type;
			
			if (mutableDeclaredType.asType() != null) {
				return mutableDeclaredType.asType();
			}
			
			//no package means primitive type
			if (mutableDeclaredType.getPackageName() == null) {
				for (TypeKind kind: TypeKind.values()) {
					if (kind.name().toLowerCase().equals(mutableDeclaredType.getSimpleName())) {
						return getPrimitiveType(kind);
					}
				}
			}

			List<TypeMirror> typeArgs = new ArrayList<TypeMirror>();

			TypeElement typeElement = elements.getTypeElement(mutableDeclaredType.getCanonicalName());
			
			//Generated, does not exists in the java world
			if (typeElement == null) {
				return null;
			}
			
			int i = 0;

			for (MutableTypeVariable typeParameter: mutableDeclaredType.getTypeVariables()) {
				
				TypeMirror typeVariable = fromMutableType(typeParameter);
				
				if (typeVariable != null) {
					typeArgs.add(typeVariable);
				} else {
					if (typeElement != null) {
						typeArgs.add(typeElement.getTypeParameters().get(i).asType());
					} else {
						//TODO It is not possible to add there a typeVariable with the specific name
						//maybe we should put there types at least
					}
				}
				
				i++;
			}

			return getDeclaredType(typeElement, typeArgs.toArray(new TypeMirror[] {}));
		}
		
		return null;
	}

	public MutableDeclaredType toMutableType(String className) {
		TypeElement typeElement = elements.getTypeElement(className);

		String genericType = null;
		
		if (typeElement == null) {
			int genericTypeIndex = className.indexOf("<");
			if (genericTypeIndex != -1) {
				genericType = className.substring(genericTypeIndex + 1);
				genericType = genericType.substring(0, genericType.length() - 1);
				className = className.substring(0,genericTypeIndex);
				typeElement = elements.getTypeElement(className);
			}
		}
		
		if (typeElement != null) {
			if (genericType != null) {
				String[] params = genericType.split(",");
				MutableTypeVariable[] parameters = new MutableTypeVariable[params.length];
				for (int i = 0; i < params.length; i++) {
					parameters[i] = toTypeVariable(params[i]);
				}

				PackageElement packageElement = elements.getPackageOf(typeElement);
				return new MutableDeclared(typeElement.asType(), packageElement.getQualifiedName().toString(), typeElement.getSimpleName().toString(), processingEnv).setTypeVariables(parameters);
			} else {
				return (MutableDeclaredType) toMutableType(typeElement.asType());
			}
		}

		return null;
	}

	private MutableTypeVariable toTypeVariable(String typeVariable) {
		int extendsIndex = typeVariable.indexOf("extends");

		MutableVariable result = new MutableVariable();

		if (extendsIndex != -1) {
			String parameterType = typeVariable.substring(extendsIndex + "extends".length()).trim();
			//TODO handle more upper bounds
			result.setVariable(typeVariable.substring(0, extendsIndex).trim());
			result.addUpperBound(toMutableType(parameterType));
			return result;
		}

		int superIndex = typeVariable.indexOf("super");

		if (superIndex != -1) {
			String parameterType = typeVariable.substring(superIndex + "super".length()).trim();
			result.setVariable(typeVariable.substring(0, superIndex).trim());
			result.addLowerBound(toMutableType(parameterType));
			return result;
		}

		MutableTypeMirror parameterType = toMutableType(typeVariable);

		if (parameterType != null) {
			result.addUpperBound(toMutableType(parameterType));
			return result;
		}

		result.setVariable(typeVariable);
		return result;
	}
		
	private boolean areSameTypes(Collection<? extends MutableTypeMirror> dtos1, Collection<? extends MutableTypeMirror> dtos2) {
		if (dtos1 == null && dtos2 == null) {
			return true;
		}
		
		if (dtos1 == null || dtos2 == null) {
			return false;
		}
		
		if (dtos1.size() != dtos2.size()) {
			return false;
		}

		Iterator<? extends MutableTypeMirror> iterator1 = dtos1.iterator();
		Iterator<? extends MutableTypeMirror> iterator2 = dtos2.iterator();

		while (iterator1.hasNext()) {
			if (!isSameType(iterator1.next(), iterator2.next())) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean implementsType(MutableTypeMirror t1, MutableTypeMirror t2) {
		if (t1 == null || t2 == null || !t1.getKind().isDeclared() || !t2.getKind().isDeclared()) {
			return false;
		}
		
		MutableDeclaredType dt1 = (MutableDeclaredType)t1;
		MutableDeclaredType dt2 = (MutableDeclaredType)t2;

		if (dt1.getCanonicalName().equals(dt2.getCanonicalName())) {
			return true;
		}
		
		for (MutableTypeMirror interfaceType: dt1.getInterfaces()) {
			
			if (interfaceType.getKind().isDeclared()) {
				if (((MutableDeclaredType)interfaceType).getCanonicalName().equals(dt2.getCanonicalName())) {
					return true;
				}
				
				if (implementsType(interfaceType, t2)) {
					return true;
				}
			}
			
		}

		MutableTypeMirror superClassType = dt1.getSuperClass();
		
		if (superClassType != null && superClassType.getKind().isDeclared()) {
			if (((MutableDeclaredType)superClassType).getCanonicalName().equals(dt2.getCanonicalName())) {
				return true;
			}

			return implementsType(superClassType, t2);
		}

		return false;
	}
	
	public MutableTypeMirror stripTypeVariableTypes(MutableTypeMirror mutableType) {
		if (mutableType instanceof MutableDeclaredType) {
			((MutableDeclaredType)mutableType).stripTypeParametersTypes();
			return mutableType;
		}
		
		if (mutableType instanceof MutableTypeVariable) {
			((MutableTypeVariable)mutableType).setLowerBounds(null);
			((MutableTypeVariable)mutableType).setUpperBounds(null);
			return mutableType;
		}
		
		if (mutableType instanceof MutableWildcardType) {
			((MutableWildcardType)mutableType).setSuperBound(null);
			((MutableWildcardType)mutableType).setExtendsBound(null);
		}
		
		if (mutableType instanceof MutableArrayType) {
			stripTypeVariableTypes(((MutableArrayType)mutableType).getComponentType());
		}
		
		return mutableType;
	}

	public ExecutableElementReference getReferenceToMethod(MutableExecutableElement executableElement, MutableReferenceType referenceType) {
		return new ExecutableReference(executableElement, referenceType);
	}

	public MutableReferenceTypeValue getReferenceValue(MutableDeclaredType declaredType, MutableReferenceType referenceType) {
		return new MutableDeclaredReferenceValue(declaredType, referenceType);
	}
	
	public MutableTypeValue getTypeValue(Object value) {
		if (value.getClass().isArray()) {
			return getArrayValue(getArrayType(toMutableType(value.getClass().getComponentType())), (Object[])value);
		}
		
		if (value.getClass().isEnum()) {
			return getEnumValue(value);
		}

		return getTypeValue(toMutableType(value.getClass()), value);
	}
	
	public MutableTypeValue getTypeValue(MutableTypeMirror type, Object value) {
		
		if (value != null && value.getClass().isEnum()) {
			return getEnumValue(value);
		}

		if (type instanceof MutableDeclaredType) {
			return getDeclaredValue((MutableDeclaredType)type, value);
		}

		if (type instanceof MutableArrayType) {
			return getArrayValue((MutableArrayType)type, value);
		}

		return null;
	}
	
	public MutableDeclaredTypeValue getEnumValue(Object value) {
		return new MutableEnumValue(toMutableType(value.getClass()), value, processingEnv);
	}

	public MutableDeclaredTypeValue getDeclaredValue(MutableDeclaredType type, Object value) {
		return new MutableDeclaredValue(type, value, processingEnv);
	}
	
	public MutableArrayTypeValue getArrayValue(MutableArrayType array, Object... values) {
		MutableTypeValue[] arrayValues = new MutableTypeValue[values.length];
		int i = 0;
		for (Object value: values) {
			arrayValues[i++] = getTypeValue(array.getComponentType(), value);
		}
		
		return new MutableArrayValue(array, arrayValues);
	}	
}