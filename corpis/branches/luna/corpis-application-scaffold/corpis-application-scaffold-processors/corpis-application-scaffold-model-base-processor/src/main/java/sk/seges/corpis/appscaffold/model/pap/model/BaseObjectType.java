package sk.seges.corpis.appscaffold.model.pap.model;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;

public class BaseObjectType extends AbstractDataType {

	private static final String SUFFIX = "Base";

	public BaseObjectType(MutableDeclaredType dataDefinition, MutableProcessingEnvironment processingEnv) {
		super(dataDefinition, processingEnv);

		List<MutableTypeMirror> interfaces = new ArrayList<MutableTypeMirror>();
		
//		Element element = ((DeclaredType)dataDefinition.asType()).asElement();
//
//		for (TypeMirror dataInterface: ((TypeElement) element).getInterfaces()) {
//			if (dataInterface.getKind().equals(TypeKind.DECLARED) && new PersistentObjectAccessor(((DeclaredType) dataInterface).asElement(), processingEnv).isEntity()) {
//				MutableDeclaredType mutableInterfaceType = (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(dataInterface);
//				
//				if (getSuperClass() == null) {
//					setSuperClass(new BaseObjectType(mutableInterfaceType, processingEnv));
//				}
//			}
//		}

		DomainDataInterfaceType domainDataInterfaceType = new DomainDataInterfaceType(dataDefinition, processingEnv);
		
		interfaces.add(domainDataInterfaceType);
		changePackage(dataDefinition.getPackageName() + "." + LocationType.SERVER.getName() + "." + LayerType.MODEL.getName() + "." + ImplementationType.BASE.getName());
		
		setInterfaces(interfaces);

		List<MutableDeclaredType> baseObjects = domainDataInterfaceType.getBaseObjects();
		
		if (baseObjects.size() == 1) {
			//TODO identify cycle references
			setSuperClass(new BaseObjectType(baseObjects.get(0), processingEnv));
		}
		
		setKind(MutableTypeKind.CLASS);
		
		setDataTypeVariables();
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return domainDataType.clone().addClassSufix(SUFFIX);
	}
}