package sk.seges.acris.mvp.client.action.mocks.registrator;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.mvp.client.action.mocks.core.MockActionHandler;

import com.philbeaudoin.gwtp.dispatch.shared.Action;


public class MockRegistrator {

	private static Map<Class<? extends Action<?>>, MockActionHandler<?, ?>> actionsMap = new HashMap<Class<? extends Action<?>>, MockActionHandler<?, ?>>();

	public static void put(Class<? extends Action<?>> actionType, MockActionHandler<?, ?> actionHandler) {
		actionsMap.put(actionType, actionHandler);
	}

	public static MockActionHandler<?, ?> get(Class<? extends Action<?>> actionType) {
		return actionsMap.get(actionType);
	}
}