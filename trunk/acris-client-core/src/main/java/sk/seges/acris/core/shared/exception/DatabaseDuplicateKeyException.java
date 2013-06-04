package sk.seges.acris.core.shared.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DatabaseDuplicateKeyException extends RuntimeException implements IsSerializable{

	private static final long serialVersionUID = 3395626486234316796L;

	public DatabaseDuplicateKeyException() {
		super();
	}

	public DatabaseDuplicateKeyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DatabaseDuplicateKeyException(String arg0) {
		super(arg0);
	}

	public DatabaseDuplicateKeyException(Throwable arg0) {
		super(arg0);
	}


}
