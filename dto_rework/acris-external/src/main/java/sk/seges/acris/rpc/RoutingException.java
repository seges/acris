/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.seges.acris.rpc;

/**
 * @author eldzi
 */
public class RoutingException extends RuntimeException {
	private static final long serialVersionUID = -8216395080279340889L;

	protected enum TYPE {
		FILE_MANIPULATION, ROUTING;
	}

	private final TYPE type;
	private final Object causingObject;

	public RoutingException(String message, TYPE type, Object causingObject, Throwable cause) {
		super(message, cause);
		this.type = type;
		this.causingObject = causingObject;
	}

	public RoutingException(String message, TYPE type, Object causingObject) {
		super(message);
		this.type = type;
		this.causingObject = causingObject;
	}

	public TYPE getType() {
		return type;
	}

	public Object getCausingObject() {
		return causingObject;
	}

	@Override
	public String getMessage() {
		if(type == null && causingObject == null)
			return super.toString();
		return super.getMessage() + " [ type = " + type.name() + ", causingObject = " + causingObject + "]";
	}
}
