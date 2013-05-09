package sk.seges.acris.widget.client.celltable.column;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface ColumnValuesRemoteLoaderAsync extends RemoteService {

	void loadColumnValues(Class<?> dataClass, String fieldName, AsyncCallback<List<String>> callback);
	
}
