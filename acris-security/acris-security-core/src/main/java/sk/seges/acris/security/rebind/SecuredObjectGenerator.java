package sk.seges.acris.security.rebind;

import sk.seges.acris.core.rebind.ReplaceByGenerator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class SecuredObjectGenerator extends ReplaceByGenerator {

	private static final String CLASS_SUFFIX = "SecurityWrapper";
	
	private TypeOracle typeOracle;
	
	@Override
	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		
		this.typeOracle = context.getTypeOracle();
		

		try {
			typeOracle.getType(typeName + CLASS_SUFFIX);
		} catch (NotFoundException e) {
			return typeName;
		}

		return typeName + CLASS_SUFFIX;
	}

}