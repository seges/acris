package sk.seges.sesam.core.test.webdriver.model;

public class ValueChangeParameter implements ActionParameter {

	private final String textBeforeChange;
	private String textAfterChange;
	
	public ValueChangeParameter(String textBeforeChange) {
		this.textBeforeChange = textBeforeChange;
	}

	public void setTextAfterChange(String textAfterChange) {
		this.textAfterChange = textAfterChange;
	}
	
	@Override
	public String toString() {
		return "from " + toValue(textBeforeChange) + " to " + toValue(textAfterChange);
	}
	
	private String toValue(String value) {
		if (value == null || value.length() == 0) {
			return "'Empty string'";
		}
		
		return "'" + value + "'";
	}
}