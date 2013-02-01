/**
 * 
 */
package sk.seges.sesam.remote.exception;

import sk.seges.sesam.remote.domain.RemoteCommand;

/**
 * @author eldzi
 */
public class RemoteInvocationException extends RuntimeException {
    private static final long serialVersionUID = 8349383302290689734L;
    private final RemoteCommand command;

    public RemoteInvocationException(String message, RemoteCommand command, Throwable cause) {
        super(message, cause);
        this.command = command;
    }

    public RemoteInvocationException(String message, RemoteCommand command) {
        super(message);
        this.command = command;
    }

    public RemoteInvocationException(String message, Throwable cause) {
        super(message, cause);
        command = null;
    }

    public RemoteInvocationException(String message) {
        super(message);
        command = null;
    }
    
    public RemoteCommand getCommand() {
        return command;
    }
}
