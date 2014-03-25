package sk.seges.acris.player.client.objects;

public enum EObjectType {
	CURSOR("C"), TOOLTIP("T");
	
	private String type;
	
	private EObjectType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public static EObjectType getObjectType(String type) {
		for (EObjectType objectType : EObjectType.values()) {
			if (objectType.equals(type)) {
				return objectType;
			}
		}
		
		return null;
	}
}