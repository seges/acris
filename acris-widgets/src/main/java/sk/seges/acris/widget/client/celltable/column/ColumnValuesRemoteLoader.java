package sk.seges.acris.widget.client.celltable.column;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ColumnValuesRemoteLoader extends RemoteService {

	Map<String, String> loadColumnValues(String dataClassName, String column, String locale, String webId);
	
}
