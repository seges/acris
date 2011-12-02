package sk.seges.sesam.core.test.selenium.report.model;


public class CommandResult {

	private SeleniumOperation operation;
	private SeleniumOperationResult result;
	private SeleniumOperationState state;
	private String screenshotName;
	
	private Object[] parameters;
	private Throwable throwable;
	
	public String getScreenshotName() {
		return screenshotName;
	}
	
	public void setScreenshotName(String screenshotName) {
		this.screenshotName = screenshotName;
	}
	
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
	
	public void setOperation(SeleniumOperation operation) {
		this.operation = operation;
	}
	
	public SeleniumOperation getOperation() {
		return operation;
	}

	public String getOperationDescription() {
		return operation.getDescription();
	}

	public void setResult(SeleniumOperationResult result) {
		this.result = result;
	}
	
	public SeleniumOperationResult getResult() {
		return result;
	}

	public void setState(SeleniumOperationState state) {
		this.state = state;
	}
	
	public SeleniumOperationState getState() {
		return state;
	}
	
	public void setParameters(Object... parameters) {
		this.parameters = parameters;
	}
	
	public Object[] getParameters() {
		return parameters;
	}
	
	public boolean isFailure() {
		return throwable != null;
	}
}