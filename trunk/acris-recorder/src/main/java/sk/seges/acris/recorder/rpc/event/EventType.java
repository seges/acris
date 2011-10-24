package sk.seges.acris.recorder.rpc.event;


public enum EventType {
	HtmlEvent(3), MouseEvent(0), KeyboardEvent(2);
	
	private int type;
	
	EventType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static EventType getEvent(int type) {
		for (EventType eventType : values()) {
			if (eventType.getType() == type) {
				return eventType;
			}
		}
		
		return null;
	}
}