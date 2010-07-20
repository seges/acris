package sk.seges.acris.core.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class ReplaceByGenerator extends AbstractGenerator{

	protected String replaceClass;
	
	public ReplaceByGenerator() {
		
	}
	
	public ReplaceByGenerator(String replaceClass) {
		this.replaceClass = replaceClass;
	}

	@Override
	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		return replaceClass;
	}
}