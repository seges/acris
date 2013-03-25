/**
 * 
 */
package sk.seges.acris.pivo;

/**
 * @author eldzi
 */
public class PivoException extends RuntimeException {
	private static final long serialVersionUID = 831170791758240467L;

	public PivoException() {
		super();
	}

	public PivoException(String message, Throwable cause) {
		super(message, cause);
	}

	public PivoException(String message) {
		super(message);
	}

	public PivoException(Throwable cause) {
		super(cause);
	}
}
