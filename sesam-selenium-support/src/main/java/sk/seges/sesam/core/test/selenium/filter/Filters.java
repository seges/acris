package sk.seges.sesam.core.test.selenium.filter;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

public class Filters {

	public static Function<WebDriver, List<WebElement>> filterNotVisible(List<WebElement> webElements) {
		return new FilterNotVisible(webElements);
	}
}
