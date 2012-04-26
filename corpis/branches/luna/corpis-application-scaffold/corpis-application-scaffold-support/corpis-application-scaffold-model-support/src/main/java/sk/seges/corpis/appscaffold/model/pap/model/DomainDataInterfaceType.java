package sk.seges.corpis.appscaffold.model.pap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;

import sk.seges.corpis.appscaffold.model.pap.accessor.BaseObjectAccessor;
import sk.seges.corpis.appscaffold.model.pap.accessor.DomainInterfaceAccessor;
import sk.seges.corpis.appscaffold.model.pap.accessor.PersistentObjectAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.domain.IMutableDomainObject;

public class DomainDataInterfaceType extends AbstractDataType {

	private static final String SUFFIX = "Data";
	
	public DomainDataInterfaceType(MutableDeclaredType dataDefinition, MutableProcessingEnvironment processingEnv) {
		super(dataDefinition, processingEnv);		

		Element element = processingEnv.getElementUtils().getTypeElement(dataDefinition.getCanonicalName());

		if (getBaseObjects().size() > 1) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "Multiple base objects interfaces are defined in " + domainDataType + ". You should define only one base object interface!", domainDataType.asElement());
		}

		List<MutableDeclaredType> interfaces = getDataInterfaces();

		if (interfaces.size() == 0) {
			if (new PersistentObjectAccessor(element, processingEnv).isEntity()) {
				MutableDeclaredType dmainObjectMutableType = processingEnv.getTypeUtils().toMutableType(IMutableDomainObject.class.getName());
				interfaces.add(dmainObjectMutableType);
			} else {
				interfaces.add(processingEnv.getTypeUtils().toMutableType(Serializable.class));
			}
		}

		changePackage(dataDefinition.getPackageName() + "." + LocationType.SERVER.getName() + "." + LayerType.MODEL.getName() + "." + ImplementationType.DATA.getName());
		
		setInterfaces(interfaces);

		setKind(MutableTypeKind.INTERFACE);
	}

	public DomainDataInterfaceType(TypeElement dataInterfaceType, MutableProcessingEnvironment processingEnv) {
		super(null, processingEnv);
		setDelegate(processingEnv.getTypeUtils().toMutableType(dataInterfaceType.asType()));
	}

	public List<MutableDeclaredType> getBaseObjects() {
		List<MutableDeclaredType> baseObjects = new ArrayList<MutableDeclaredType>();
		
		for (MutableTypeMirror domainInterface : domainDataType.getInterfaces()) {
			MutableDeclaredType d = (MutableDeclaredType) domainInterface;
			if (new BaseObjectAccessor(((DeclaredType)d.asType()).asElement(), processingEnv).isValid()) {
				baseObjects.add(d);
			}
		}
		
		return baseObjects;
	}
	
	public List<MutableDeclaredType> getDataInterfaces() {
		List<MutableDeclaredType> interfaces = new ArrayList<MutableDeclaredType>();
		
		for (MutableTypeMirror domainInterface : domainDataType.getInterfaces()) {
			MutableDeclaredType d = (MutableDeclaredType) domainInterface;
			if (new DomainInterfaceAccessor(((DeclaredType)d.asType()).asElement(), processingEnv).isValid()) {
				DomainDataInterfaceType domainDataInterfaceType = new DomainDataInterfaceType((MutableDeclaredType)domainInterface, processingEnv);
				interfaces.add(domainDataInterfaceType);
			} else {
				interfaces.add(d);
			}
		
		}
		
		return interfaces;
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return domainDataType.clone().addClassSufix(SUFFIX);
	}	
}