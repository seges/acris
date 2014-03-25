package sk.seges.acris.player.client.objects;

public enum EObjectActionType {
	CLICK("C"), MOVE("M"), WAIT("W"), CLICK_AND_WAIT("CW");
	
	private final String type;
	
	private EObjectActionType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public static EObjectActionType getObjectType(String type) {
		for (EObjectActionType actionType : EObjectActionType.values()) {
			if (actionType.equals(type)) {
				return actionType;
			}
		}
		
		return null;
	}

}
