package sk.seges.acris.recorder.rpc.listener;

public interface EventListener {
	void onFailure();

	void onSuccess();
}
