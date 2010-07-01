package sk.seges.acris.recorder.rpc.event;

public interface IRecordableEvent {
	public static final String ELEMENT_ID_NAME = "elementID";

	static final int ENCODE_LENGTH_SHIFT = 31;
	static final int ENCODE_EVENT_TYPE_SHIFT = 30;
}
