package sk.seges.sesam.core.test.webdriver.factory;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;

import com.thoughtworks.selenium.Selenium;

public interface SeleniumFactory {

    Selenium createSelenium(SeleniumSettings testEnvironment);

}
