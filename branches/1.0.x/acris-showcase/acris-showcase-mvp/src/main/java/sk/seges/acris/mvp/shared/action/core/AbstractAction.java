package sk.seges.acris.mvp.shared.action.core;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.philbeaudoin.gwtp.dispatch.shared.ActionImpl;
import com.philbeaudoin.gwtp.dispatch.shared.Result;

public abstract class AbstractAction<R extends Result> extends ActionImpl<R> implements IsSerializable {

	private static final long serialVersionUID = 705022393629621641L;

	public static final String SERVICE_NAME = "service/command";

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
}