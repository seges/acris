package sk.seges.corpis.core.pap.dao.printer;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class EntityInstancerPrinter implements DaoPrinter {

	private FormattedPrintWriter pw;
	
	public EntityInstancerPrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}
	
	@Override
	public void print(DaoContext context) {
		
//		DaoApiType daoInterface = context.getDaoType().getDaoInterface();
		
//		ExecutableElement method = getMethodByReturnType(context.getInterfaceElement(), context.getDomainType(), context.getProcessingEnv().getTypeUtils());
//		if (method == null) {
//			return;
//		}
		pw.println("@Override");
		pw.println("public " + context.getDomainType().getSimpleName() + " getEntityInstance() {");
		pw.println("return new " + context.getDomainType().getSimpleName().toString() + "();");
		pw.println("}");
	}

//	private ExecutableElement getMethodByReturnType(TypeElement owner, MutableDeclaredType returnType, MutableTypes types) {
//		List<ExecutableElement> methods = ElementFilter.methodsIn(owner.getEnclosedElements());
//		
//		for (ExecutableElement method : methods) {
//			if (method.getReturnType() != null) {
//				//
//				if (method.getReturnType().getKind().equals(TypeKind.TYPEVAR)) {
//					TypeVariable typeVariable = (TypeVariable)method.getReturnType();
//					if (types.isAssignable(returnType.asType(), typeVariable.getUpperBound())) {
//						return method;
//					}
//				} else {
//					if (types.isAssignable(returnType.asType(), method.getReturnType())) {
//						return method;
//					}
//				}
//			}
//		}
//		
//		return null;
//	}
}
