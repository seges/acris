package sk.seges.acris.json.jsr269.model;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.json.client.data.IJsonObject;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class JsonizerType extends DelegateMutableDeclaredType {

	public static final String OUTPUT_SUFFIX = "Jsonizer";

	private MutableDeclaredType type;
	
	public JsonizerType(MutableDeclaredType type, MutableProcessingEnvironment processingEnv) {
		this.type = type;
		Set<MutableTypeMirror> interfaces = new HashSet<MutableTypeMirror>();
		interfaces.add(processingEnv.getTypeUtils().toMutableType(IJsonObject.class).setTypeVariables(processingEnv.getTypeUtils().getTypeVariable(null, type)));

		setInterfaces(interfaces);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return type.clone().addClassSufix(OUTPUT_SUFFIX);
	}
}