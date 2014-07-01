package sk.seges.acris.widget.client.celltable.column;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ColumnValuesRemoteLoaderAsync{

	void loadColumnValues(String dataClassName, String fieldName, String locale, String webId, AsyncCallback<Map<String, String>> callback);
	
}
