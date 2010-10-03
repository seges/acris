package sk.seges.acris.recorder.rpc.bean;

import com.google.gwt.core.client.GWT;

public class BeanAccessor {

	public static void init() {
		if (!GWT.isScript()) {
			return;
		}
		GWT.create(BeanAccessor.class);
	}
}