package sk.seges.acris.security.rebind;

import com.google.gwt.core.ext.*;
import sk.seges.acris.security.client.annotations.ManagedSecurity;
import sk.seges.acris.security.client.annotations.RuntimeSecurity;
import sk.seges.acris.security.client.annotations.Secured;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class SecuredObjectGenerator extends IncrementalGenerator {

	private static final String SECURITY_CLASS_SUFFIX = "SecurityWrapper";
	private static final String MANAGEABLE_CLASS_SUFFIX = "ManageableSecured";
	private static final String RUNTIME_CLASS_SUFFIX = "RuntimeSecured";
	
	private TypeOracle typeOracle;
	
	@Override
	public RebindResult generateIncrementally(TreeLogger treeLogger, GeneratorContext context, String typeName) throws UnableToCompleteException {

		this.typeOracle = context.getTypeOracle();
		
		JClassType type = typeOracle.findType(typeName);

		if (isSecured(type)) {
			try {
				return new RebindResult(RebindMode.USE_EXISTING, typeOracle.getType(type.getPackage().getName() + "." + type.getSimpleSourceName() + SECURITY_CLASS_SUFFIX).getQualifiedSourceName());
			} catch (NotFoundException e) {
				return new RebindResult(RebindMode.USE_EXISTING, typeName);
			}
		}

		if (isRuntimeSecured(type)) {
			try {
				return new RebindResult(RebindMode.USE_EXISTING, typeOracle.getType(type.getPackage().getName() + "." + type.getSimpleSourceName() + RUNTIME_CLASS_SUFFIX).getQualifiedSourceName());
			} catch (NotFoundException e) {
				return new RebindResult(RebindMode.USE_EXISTING, typeName);
			}
		}

		if (isManageableSecured(type)) {
			try {
				return new RebindResult(RebindMode.USE_EXISTING, typeOracle.getType(type.getPackage().getName() + "." + type.getSimpleSourceName() + MANAGEABLE_CLASS_SUFFIX).getQualifiedSourceName());
			} catch (NotFoundException e) {
				return new RebindResult(RebindMode.USE_EXISTING, typeName);
			}
		}

		return new RebindResult(RebindMode.USE_EXISTING, typeName);
	}

	protected boolean isSecured(JClassType classType) {
		return classType.getAnnotation(Secured.class) != null;
	}

	protected boolean isRuntimeSecured(JClassType classType) {
		return classType.getAnnotation(RuntimeSecurity.class) != null;
	}

	protected boolean isManageableSecured(JClassType classType) {
		return classType.getAnnotation(ManagedSecurity.class) != null;
	}

	@Override
	public long getVersionId() {
		return 0;
	}
}