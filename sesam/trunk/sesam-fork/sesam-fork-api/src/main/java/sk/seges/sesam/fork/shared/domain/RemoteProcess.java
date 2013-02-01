/**
 * 
 */
package sk.seges.sesam.fork.shared.domain;

import java.io.Serializable;

/**
 * @author ladislav.gazo
 */
public class RemoteProcess implements Serializable {
	private static final long serialVersionUID = 5025163179890306769L;

	private final String command;

	public RemoteProcess(String command) {
		super();
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	@Override
	public String toString() {
		return "RemoteProcess [command=" + command + "]";
	}
}
