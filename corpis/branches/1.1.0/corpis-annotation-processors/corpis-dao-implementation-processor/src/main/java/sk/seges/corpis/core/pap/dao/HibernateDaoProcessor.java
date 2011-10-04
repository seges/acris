package sk.seges.corpis.core.pap.dao;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.type.DeclaredType;

import sk.seges.corpis.core.pap.dao.configurer.HibernateDaoProcessorConfigurer;
import sk.seges.corpis.core.pap.dao.model.HibernateDaoType;
import sk.seges.corpis.core.pap.dao.printer.CrudPrinter;
import sk.seges.corpis.core.pap.dao.printer.DaoContext;
import sk.seges.corpis.core.pap.dao.printer.DaoPrinter;
import sk.seges.corpis.core.pap.dao.printer.EntityInstancerPrinter;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateDaoProcessor extends MutableAnnotationProcessor {
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new HibernateDaoProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new HibernateDaoType(context.getMutableType(), processingEnv)
		};
	}
	
	protected DaoPrinter[] getDaoPrinters(FormattedPrintWriter pw) {
		return new DaoPrinter[] {
			new CrudPrinter(pw),
			new EntityInstancerPrinter(pw)
		};
	}
	
	@Override
	protected void processElement(ProcessorContext context) {

		HibernateDaoType hibernateDaoType = new HibernateDaoType(processingEnv.getTypeUtils().toMutableType((DeclaredType)context.getTypeElement().asType()), processingEnv);
//		DaoApiType daoInterface = hibernateDaoType.getDaoInterface();
		
		DaoContext daoContext = new DaoContext(processingEnv.getTypeUtils().toMutableType((DeclaredType)context.getTypeElement().asType()), hibernateDaoType, processingEnv);

		for (DaoPrinter daoPrinter: getDaoPrinters(context.getPrintWriter())) {
			
//			for (TypeMirror typeMirror: typeInterfaceElement.getInterfaces()) {
//				Element interfaceElement = processingEnv.getTypeUtils().asElement(typeMirror);
//				if (interfaceElement.getKind().equals(ElementKind.INTERFACE)) {
					daoPrinter.print(daoContext);
					context.getPrintWriter().println();
				}
//			}
//		}
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		DataAccessObject daoAnnotation = context.getTypeElement().getAnnotation(DataAccessObject.class);
		
		if (daoAnnotation == null) {
			return false;
		}
		
		return daoAnnotation.provider().equals(Provider.HIBERNATE);
	}
}