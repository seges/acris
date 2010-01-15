package sk.seges.acris.rpc.listener;

public interface EventListener {
	void onFailure();

	void onSuccess();
}
