package sk.seges.acris.security.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

import sk.seges.acris.core.rebind.AbstractGenerator;

public class ManageableSecuredObjectGenerator extends AbstractGenerator {

    @Override
    public String doGenerate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
        ManageableSecuredObjectCreator manageableSecuredObjectCreator = new ManageableSecuredObjectCreator(new SecuredAnnotationProcessor());
        return manageableSecuredObjectCreator.doGenerate(logger, context, typeName, this.superclassName);
    }

}
