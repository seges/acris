package sk.seges.corpis.core.pap.dao;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.core.pap.dao.accessor.DataAccessObjectAccessor;
import sk.seges.corpis.core.pap.dao.model.AbstractHibernateDaoType;
import sk.seges.corpis.core.pap.dao.printer.CrudPrinter;
import sk.seges.corpis.core.pap.dao.printer.DaoContext;
import sk.seges.corpis.core.pap.dao.printer.DaoPrinter;
import sk.seges.corpis.core.pap.dao.printer.EntityInstancerPrinter;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public abstract class AbstractHibernateDaoProcessor extends MutableAnnotationProcessor{
		
	protected abstract AbstractHibernateDaoType getHibernateDaoType(MutableDeclaredType mutableType, MutableProcessingEnvironment processingEnv);
	
	protected DaoPrinter[] getDaoPrinters(FormattedPrintWriter pw) {
		return new DaoPrinter[] {
			new CrudPrinter(pw),
			new EntityInstancerPrinter(pw)
		};
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		AbstractHibernateDaoType hibernateDaoType = getHibernateDaoType(context.getMutableType(), processingEnv);
		DaoContext daoContext = new DaoContext(context.getMutableType(), hibernateDaoType, processingEnv);

		for (DaoPrinter daoPrinter: getDaoPrinters(context.getPrintWriter())) {
			daoPrinter.print(daoContext);
			context.getPrintWriter().println();
		}
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		if (alreadyExists) {
			return false;
		}
		
		Provider provider = new DataAccessObjectAccessor(context.getMutableType(), processingEnv).getProvider();
		return provider != null && provider.equals(Provider.HIBERNATE);
	}
}