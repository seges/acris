package sk.seges.sesam.core.pap.model.mutable.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.InitializableValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementKind;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementType;

abstract class MutableElement implements MutableElementType {

	protected final MutableProcessingEnvironment processingEnv;
	protected final MutableElementKind kind;
	protected Set<Modifier> modifiers;

	protected final InitializableValue<MutableTypeMirror> type = new InitializableValue<MutableTypeMirror>();
	protected final InitializableValue<String> simpleName = new InitializableValue<String>();

	public MutableElement(MutableElementKind kind, MutableTypeMirror type, String name, MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.kind = kind;
		this.type.setValue(type);
		this.simpleName.setValue(name);
	}

	protected MutableElement(MutableElementKind kind, MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.kind = kind;
	}

	@Override
	public MutableTypeMirror asType() {
		if (!type.isInitialized()) {
			initializeType();
		}
		return type.getValue();
	}

	@Override
	public MutableElementKind getKind() {
		return kind;
	}

	public Set<Modifier> ensureModifiers() {
		if (modifiers == null) {
			modifiers = new HashSet<Modifier>();
		}
		return modifiers;
	}

	@Override
	public Set<Modifier> getModifiers() {
		return Collections.unmodifiableSet(ensureModifiers());
	}

	public MutableElementType setModifier(Modifier... modifiers) {
		ensureModifiers().clear();
		return addModifier(modifiers);
	}
	
	@Override
	public MutableElementType addModifier(Modifier... modifiers) {
		Set<Modifier> result = new HashSet<Modifier>();
		
		ModifierConverter modifierConverter = new ModifierConverter();
		
		for (Modifier modifier: modifiers) {
			for (Modifier mod: ensureModifiers()) {
				if (modifierConverter.getModifierType(mod) != modifierConverter.getModifierType(modifier)) {
					result.add(mod);
				}
			}
			result.add(modifier);
		}
		
		this.modifiers = result;
		
		return this;
	}

	protected void initializeSimpleName() {};
	protected void initializeType() {};
	
	@Override
	public String getSimpleName() {
		if (!simpleName.isInitialized()) {
			initializeSimpleName();
		}
		return simpleName.getValue();
	}

	@Override
	public MutableElementType getEnclosingElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MutableElementType> getEnclosedElements() {
		// TODO Auto-generated method stub
		return null;
	}

}