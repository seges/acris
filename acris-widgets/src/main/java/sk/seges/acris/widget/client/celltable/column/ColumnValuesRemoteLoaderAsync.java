package sk.seges.acris.widget.client.celltable.column;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ColumnValuesRemoteLoaderAsync{

	void loadColumnValues(String dataClassName, String fieldName, AsyncCallback<List<String>> callback);
	
}
