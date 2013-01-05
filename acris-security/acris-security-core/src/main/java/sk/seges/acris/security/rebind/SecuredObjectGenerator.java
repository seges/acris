package sk.seges.acris.security.rebind;

import sk.seges.acris.core.rebind.ReplaceByGenerator;
import sk.seges.acris.security.client.annotations.ManagedSecurity;
import sk.seges.acris.security.client.annotations.RuntimeSecurity;
import sk.seges.acris.security.client.annotations.Secured;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class SecuredObjectGenerator extends ReplaceByGenerator {

	private static final String SECURITY_CLASS_SUFFIX = "SecurityWrapper";
	private static final String MANAGEABLE_CLASS_SUFFIX = "ManageableSecured";
	private static final String RUNTIME_CLASS_SUFFIX = "RuntimeSecured";
	
	private TypeOracle typeOracle;
	
	@Override
	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		
		this.typeOracle = context.getTypeOracle();
		
		JClassType type = typeOracle.findType(typeName);

		if (isSecured(type)) {
			try {
				return typeOracle.getType(typeName + SECURITY_CLASS_SUFFIX).getQualifiedSourceName();
			} catch (NotFoundException e) {
				return typeName;
			}
		}

		if (isRuntimeSecured(type)) {
			try {
				return typeOracle.getType(typeName + RUNTIME_CLASS_SUFFIX).getQualifiedSourceName();
			} catch (NotFoundException e) {
				return typeName;
			}
		}

		if (isManageableSecured(type)) {
			try {
				return typeOracle.getType(typeName + MANAGEABLE_CLASS_SUFFIX).getQualifiedSourceName();
			} catch (NotFoundException e) {
				return typeName;
			}
		}

		return typeName;
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
}