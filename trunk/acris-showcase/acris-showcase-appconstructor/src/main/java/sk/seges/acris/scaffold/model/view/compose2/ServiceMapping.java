package sk.seges.acris.scaffold.model.view.compose2;

import sk.seges.acris.scaffold.service.ServiceOperation;

public @interface ServiceMapping {
	public static final String READ_TYPE = "read";
	public static final String CREATE_TYPE = "create";
	public static final String UPDATE_TYPE = "update";
	public static final String DELETE_TYPE = "delete";
	
	String type();
	Class<? extends ServiceOperation> operation();
}
