package sk.seges.sesam.core.pap.model.mutable.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import sk.seges.sesam.core.pap.model.InitializableValue;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;
import sk.seges.sesam.core.pap.writer.MethodPrintWriter;

class MutableMethod extends MutableType implements MutableExecutableType {

	private final MutableProcessingEnvironment processingEnv;
	private final ExecutableType methodType;
	
	private InitializableValue<MutableTypeMirror> returnType = new InitializableValue<MutableTypeMirror>();
	private InitializableValue<List<MutableTypeMirror>> thrownTypes = new InitializableValue<List<MutableTypeMirror>>();
	private InitializableValue<List<MutableTypeVariable>> typeVariables = new InitializableValue<List<MutableTypeVariable>>();
	private InitializableValue<List<MutableVariableElement>> parameters = new InitializableValue<List<MutableVariableElement>>();
	private InitializableValue<String> simpleName = new InitializableValue<String>();
	
	private List<Modifier> modifiers = new LinkedList<Modifier>();

	private boolean isDefault = true;
	
	MutableMethod(MutableProcessingEnvironment processingEnv, ExecutableType methodType) {
		this.methodType = methodType;
		this.processingEnv = processingEnv;
	}

	MutableMethod(MutableProcessingEnvironment processingEnvironment, String simpleName) {
		this.methodType = null;
		this.simpleName.setValue(simpleName);
		this.processingEnv = processingEnvironment;
	}
	
	private void dirty() {
		isDefault = false;
	}
	
	@Override
	public MutableTypeKind getKind() {
		return MutableTypeKind.METHOD;
	}

	@Override
	public MutableTypeMirror getReturnType() {
		if (!this.returnType.isInitialized()) {
			if (methodType != null) {
				this.returnType.setValue(processingEnv.getTypeUtils().toMutableType(methodType.getReturnType()));
			} else {
				this.returnType.setValue(processingEnv.getTypeUtils().toMutableType(Void.class));
			}
		}
		return this.returnType.getValue();
	}

	@Override
	public MutableExecutableType setReturnType(MutableTypeMirror type) {
		dirty();
		this.returnType.setValue(type);
		return this;
	}

	@Override
	public List<MutableTypeMirror> getThrownTypes() {
		if (!this.thrownTypes.isInitialized()) {
			thrownTypes.setValue(new ArrayList<MutableTypeMirror>());
			if (methodType != null) {
				List<? extends TypeMirror> throwns = methodType.getThrownTypes();
				for (TypeMirror thrown: throwns) {
					addThrownType(processingEnv.getTypeUtils().toMutableType(thrown));
				}
			}
		}
		
		return Collections.unmodifiableList(this.thrownTypes.getValue());
	}

	@Override
	public MutableExecutableType setThrownTypes(List<MutableTypeMirror> thrownTypes) {
		dirty();
		this.thrownTypes.setValue(thrownTypes);
		return this;
	}

	@Override
	public MutableExecutableType addThrownType(MutableTypeMirror thrownType) {
		dirty();
		thrownTypes.getValue().add(thrownType);
		return this;
	}

	@Override
	public List<MutableTypeVariable> getTypeVariables() {
		if (!this.typeVariables.isInitialized()) {
			typeVariables.setValue(new ArrayList<MutableTypeVariable>());
			if (methodType != null) {
				List<? extends TypeVariable> variables = methodType.getTypeVariables();
				for (TypeVariable variable: variables) {
					typeVariables.getValue().add((MutableTypeVariable) processingEnv.getTypeUtils().toMutableType(variable));
				}
			}
		}
		
		return Collections.unmodifiableList(this.typeVariables.getValue());
	}

	@Override
	public MutableExecutableType setTypeVariables(List<MutableTypeVariable> variables) {
		dirty();
		this.typeVariables.setValue(variables);
		return this;
	}

	private List<MutableVariableElement> ensureParameters() {
		if (!this.parameters.isInitialized()) {
			parameters.setValue(new ArrayList<MutableVariableElement>());
			if (methodType != null) {
				List<? extends TypeMirror> params = methodType.getParameterTypes();
				for (TypeMirror param: params) {
					parameters.getValue().add(processingEnv.getElementUtils().getParameterElement(processingEnv.getTypeUtils().toMutableType(param), null));
				}
			} else {
				parameters.setValue(new ArrayList<MutableVariableElement>());
			}
		}
		
		return parameters.getValue();
	}
	
	@Override
	public List<MutableVariableElement> getParameters() {
		return Collections.unmodifiableList(ensureParameters());
	}

	@Override
	public MutableExecutableType setParameters(List<MutableVariableElement> params) {
		dirty();
		this.parameters.setValue(params);
		return this;
	}

	public String getSimpleName() {
		if (!this.simpleName.isInitialized()) {
			if (this.methodType == null) {
				throw new RuntimeException("Method simple name nor " + ExecutableType.class.getSimpleName() + " was defined");
			}
			if (this.methodType.getKind().equals(TypeKind.DECLARED)) {
				this.simpleName.setValue(((DeclaredType)methodType).asElement().getSimpleName().toString());
			} else {
				this.simpleName.setValue(methodType.toString());
			}
		}
		
		return this.simpleName.getValue();
	}
	
	@Override
	public MutableExecutableType setSimpleName(String simpleName) {
		dirty();
		this.simpleName.setValue(simpleName);
		return this;
	}

	@Override
	public String toString(ClassSerializer serializer) {
		return toString(serializer, false);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		
		String result = "";
		
		/*
		for (Modifier modifier: getModifiers()) {
			if (result.length() > 0) {
				result += " ";
			}
			result += modifier;
		}

		if (result.length() > 0) {
			result += " ";
		}
		*/
		List<MutableTypeVariable> typeVariables = getTypeVariables();
		
		if (typeVariables.size() > 0) {
			result += "<";

			int i = 0;
			
			for (MutableTypeVariable variable: typeVariables) {
				if (i > 0) {
					result += ", ";
				}
				result += variable.toString(serializer, typed);
				i++;
			}
			
			result += "> ";
		}
		
		result += getReturnType().toString(serializer, typed) + " ";
		result += getSimpleName() + "(";
		
		int i = 0;
		for (MutableVariableElement parameter: getParameters()) {
			if (i > 0) {
				result += ", ";
			}
			result += parameter.asType().toString(serializer, typed) + " " + parameter.getSimpleName();
			
			if (parameter.getSimpleName() != null) {
				result += parameter.getSimpleName();
			} else {
				result += "arg" + i++;
			}
		}
		
		return result + ")";
	}

	@Override
	public MutableExecutableType clone() {
		MutableMethod result = null;
		
		if (methodType != null) {
			result = new MutableMethod(processingEnv, methodType);
		} else {
			result = new MutableMethod(processingEnv, this.simpleName.getValue());
		}
		
		if (returnType.isInitialized()) {
			result.returnType.setValue(returnType.getValue());
		}
		
		if (thrownTypes.isInitialized()) {
			result.thrownTypes.setValue(thrownTypes.getValue());
		}
		
		if (typeVariables.isInitialized()) {
			result.typeVariables.setValue(typeVariables.getValue());
		}
		
		if (parameters.isInitialized()) {
			result.parameters.setValue(parameters.getValue());
		}
		
		if (simpleName.isInitialized()) {
			result.simpleName.setValue(simpleName.getValue());
		}
		
		return result;
	}

	private HierarchyPrintWriter printWriter;

	@Override
	public HierarchyPrintWriter getPrintWriter(HierarchyPrintWriter hierarchyPrintWriter) {
		if (printWriter == null) {
			dirty();
			printWriter = new MethodPrintWriter(this, processingEnv, hierarchyPrintWriter);
		}
		
		return printWriter;
	}

	@Override
	public List<Modifier> getModifiers() {
		return Collections.unmodifiableList(modifiers);
	}

	@Override
	public MutableExecutableType setModifier(Modifier... modifiers) {
		dirty();
		this.modifiers.clear();
		return addModifier(modifiers);
	}
	
	@Override
	public MutableExecutableType addModifier(Modifier... modifiers) {
		dirty();
		List<Modifier> result = new ArrayList<Modifier>();

		ModifierConverter modifierConverter = new ModifierConverter();
		
		for (Modifier modifier: modifiers) {
			for (Modifier mod: this.modifiers) {
				if (modifierConverter.getModifierType(mod) != modifierConverter.getModifierType(modifier)) {
					result.add(mod);
				}
			}
			result.add(modifier);
		}
		
		this.modifiers = result;
		return this;
	}

	@Override
	public MutableExecutableType addParameter(MutableVariableElement parameter) {
		dirty();
		ensureParameters().add(parameter);
		return this;
	}

	@Override
	public boolean isDefault() {
		return isDefault;
	}
}