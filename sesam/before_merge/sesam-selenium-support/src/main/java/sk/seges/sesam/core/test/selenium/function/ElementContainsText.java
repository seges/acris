package sk.seges.sesam.core.test.selenium.function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ElementContainsText extends AbstractElementFunction {

	protected final String text;
	
	public ElementContainsText(By locator, boolean multiple, String text) {
		super(locator, multiple);
		this.text = text;
	}

	@Override
	protected boolean isElementSuitable(WebElement webElement) {
		String text = webElement.getText();
		return text != null && text.contains(this.text);
	}
}