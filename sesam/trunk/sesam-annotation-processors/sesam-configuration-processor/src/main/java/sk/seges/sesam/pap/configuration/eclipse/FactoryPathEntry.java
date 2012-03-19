package sk.seges.sesam.pap.configuration.eclipse;

public class FactoryPathEntry {

	private String kind;

	private String id;
	
	private boolean enabled;
	
	private boolean runInBatchMode;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isRunInBatchMode() {
		return runInBatchMode;
	}

	public void setRunInBatchMode(boolean runInBatchMode) {
		this.runInBatchMode = runInBatchMode;
	}
}