/**
 * 
 */
package sk.seges.sesam.fork.shared.domain;

import java.io.Serializable;

/**
 * @author ladislav.gazo
 */
public class RemoteProcessResponse implements Serializable {
	private static final long serialVersionUID = -1712521545357800632L;
	
	private String output;
	private String error;
	private int exitStatus;

	public RemoteProcessResponse(String output, String error, int exitStatus) {
		super();
		this.output = output;
		this.error = error;
		this.exitStatus = exitStatus;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getExitStatus() {
		return exitStatus;
	}
	public void setExitStatus(int exitStatus) {
		this.exitStatus = exitStatus;
	}
	@Override
	public String toString() {
		return "RemoteProcessResponse [output=" + output + ", error=" + error
				+ ", exitStatus=" + exitStatus + "]";
	}
}
