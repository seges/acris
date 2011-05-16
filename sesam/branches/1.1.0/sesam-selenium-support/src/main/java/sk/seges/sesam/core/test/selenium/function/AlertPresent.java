package sk.seges.sesam.core.test.selenium.function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class AlertPresent implements ExpectedCondition<Alert> {

	private String text;

	AlertPresent(String text) {
		this.text = text;
	}

	@Override
	public Alert apply(WebDriver webDriver) {
		Alert alert = webDriver.switchTo().alert();
		try {
			if (alert != null && alert.getText() != null && alert.getText().contains(text)) {
				return alert;
			}
		} catch (Exception ex) {
		}
		return null;
	}
}