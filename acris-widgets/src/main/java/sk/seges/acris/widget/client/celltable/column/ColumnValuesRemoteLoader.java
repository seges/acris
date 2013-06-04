package sk.seges.acris.widget.client.celltable.column;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ColumnValuesRemoteLoader extends RemoteService {

	List<String> loadColumnValues(Class<?> dataClass, String column);
	
}
