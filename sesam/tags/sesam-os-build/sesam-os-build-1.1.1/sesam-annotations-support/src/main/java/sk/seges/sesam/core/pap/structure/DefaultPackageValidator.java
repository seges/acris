package sk.seges.sesam.core.pap.structure;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

public class DefaultPackageValidator implements PackageValidator {

	protected DefaultPackageValidator() {};
	
	public enum PackageLevel {
		GROUP(true), ARTIFACT(true), LOCATION(true), BUSINESS(false), LAYER(true), TYPE(false); 
		
		private boolean required;
		
		PackageLevel(boolean required) {
			this.required = required;
		}
		
		public boolean isRequired() {
			return required;
		}
	}
	
	public enum LocationType implements SubPackageType {
		CLIENT("client"), SERVER("server"), SHARED("shared"), REBIND("rebind"), PAP("pap");
		
		private String name;

		LocationType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}

	public enum LayerType implements SubPackageType {
		MODEL("model"), DAO("dao"), SERVICE("service") ;

		private String name;

		LayerType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public enum ImplementationType implements SubPackageType {
		API("api"), HIBERNATE("hibernate"), TWIG_PERSIST("twig"), SPRING("spring");

		private String name;

		ImplementationType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	private LocationType locationType;
	private LayerType layerType;
	private String group;
	private String artifact;
	private String business;
	private String type;

	private String notParsedName;
	private boolean parsed = false;
	
	DefaultPackageValidator(String packageName) {
		this.notParsedName = packageName;
	}

	DefaultPackageValidator(NamedType inputClass) {
		this(inputClass.getPackageName());
	}

	public boolean isValid() {
		processPackage();
		return group != null && artifact != null && locationType != null && layerType != null;
	}
	
	public LocationType getLocationType() {
		processPackage();
		return locationType;
	}
	public LayerType getLayerType() {
		processPackage();
		return layerType;
	}
	
	public String getGroup() {
		processPackage();
		return group;
	}
	
	public String getArtifact() {
		processPackage();
		return artifact;
	}
	
	public String getBusiness() {
		processPackage();
		return business;
	}
	
	public String getType() {
		processPackage();
		return type;
	}

	public DefaultPackageValidator moveTo(LocationType locationType) {
		if (isValid()) {
			this.locationType = locationType;
		}
		return this;
	}

	public DefaultPackageValidator append(ImplementationType type) {
		if (isValid()) {
			this.type += "." + type.getName();
		} else {
			this.notParsedName += "." + type.getName();
		}
		return this;
	}

	public DefaultPackageValidator setType(String type) {
		if (isValid()) {
			this.type += "." + type;
		} else {
			this.notParsedName += "." + type;
		}
		return this;
	}

	public DefaultPackageValidator moveTo(LayerType layerType) {
		if (isValid()) {
			this.layerType = layerType;
		}
		return this;
	}
	
	protected void processPackage() {
		
		if (parsed) {
			return;
		}
		
		parsed = true;
		
		int index = -1;
		for (LocationType locationType: LocationType.values()) {
			index = notParsedName.indexOf("." + locationType.getName() + ".");
			if (index != -1) {
				this.locationType = locationType;
				break;
			}
		}
		
		if (index == -1) {
			return;
		}
		
		String packageName = notParsedName;
		
		String[] packageParts = packageName.substring(0, index).split("\\.");
		if (packageParts.length == 0) {
			return;
		}
		
		this.artifact = packageParts[packageParts.length - 1];
		this.group = "";
		for (int i = 0; i < packageParts.length - 1; i++) {
			if (i > 0) {
				this.group += ".";
			}
			this.group += packageParts[i];
		}
		
		packageName = "." + packageName.substring(index + this.locationType.getName().length() + 2) + ".";
		
		index = -1;
		
		for (LayerType layerType: LayerType.values()) {
			index = packageName.indexOf("." + layerType.getName() + ".");
			if (index != -1) {
				this.layerType = layerType;
				break;
			}
		}

		if (index == -1) {
			return;
		}
		
		if (index > 0) {
			this.business = packageName.substring(1, index);
		}
		
		if (index + this.layerType.getName().length() + 2 < packageName.length()) {
			this.type = packageName.substring(index + this.layerType.getName().length() + 2);
			this.type = this.type.substring(0, this.type.length() - 1);
		}
	}
	
	@Override
	public String toString() {
		if (isValid()) {
			return group + "." + artifact + "." + locationType.getName() + "." + 
					(business == null ? "" : (business + ".")) + layerType.getName() + (type == null ? "" : ("." + type));
		}
		return notParsedName;
	}

	@Override
	public PackageValidator moveTo(SubPackageType subPackageType) {
		processPackage();
		if (subPackageType instanceof LocationType) {
			return moveTo((LocationType)subPackageType);
		}
		if (subPackageType instanceof LayerType) {
			return moveTo((LayerType)subPackageType);
		}
		return this;
	}
}