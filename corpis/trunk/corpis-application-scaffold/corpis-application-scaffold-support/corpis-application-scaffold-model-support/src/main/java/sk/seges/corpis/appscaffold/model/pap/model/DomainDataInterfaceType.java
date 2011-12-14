package sk.seges.corpis.appscaffold.model.pap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

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
	private boolean hierarchy = false;
	
	public DomainDataInterfaceType(MutableDeclaredType dataDefinition, MutableProcessingEnvironment processingEnv) {
		super(dataDefinition, processingEnv);		

		Element element = processingEnv.getElementUtils().getTypeElement(dataDefinition.getCanonicalName());
		//Element element = ((DeclaredType)dataDefinition.asType()).asElement();

		List<MutableTypeMirror> interfaces = getDataInterfaces();
		
		if (dataDefinition.getInterfaces().size() == 0) {
			hierarchy = true;
			if (new PersistentObjectAccessor(element, processingEnv).isEntity()) {
				MutableDeclaredType dmainObjectMutableType = processingEnv.getTypeUtils().toMutableType(IMutableDomainObject.class.getName());
				interfaces.add(dmainObjectMutableType);
			} else {
				interfaces.add(processingEnv.getTypeUtils().toMutableType(Serializable.class));
			}
		}

		changePackage(dataDefinition.getPackageName() + "." + LocationType.SHARED.getName() + "." + LayerType.MODEL.getName() + "." + ImplementationType.DATA.getName());
		
		setInterfaces(interfaces);

		setKind(MutableTypeKind.INTERFACE);
	}

	public DomainDataInterfaceType(TypeElement dataInterfaceType, MutableProcessingEnvironment processingEnv) {
		super(null, processingEnv);
		setDelegate(processingEnv.getTypeUtils().toMutableType(dataInterfaceType.asType()));
	}
	
	public boolean isHierarchy() {
		return hierarchy;
	}
	
	private List<MutableTypeMirror> getDataInterfaces() {
		List<MutableTypeMirror> interfaces = new ArrayList<MutableTypeMirror>();
		
		for (MutableTypeMirror domainInterface : domainDataType.getInterfaces()) {
			MutableDeclaredType d = (MutableDeclaredType) domainInterface;
			if (new DomainInterfaceAccessor(((DeclaredType)d.asType()).asElement(), processingEnv).isValid()) {
				domainInterface = new DomainDataInterfaceType((MutableDeclaredType)domainInterface, processingEnv);
			}
		
			interfaces.add(domainInterface);
		}
		
		return interfaces;
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return domainDataType.clone().addClassSufix(SUFFIX);
	}	
}