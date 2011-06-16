package sk.seges.sesam.core.test.selenium.factory;

import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;

import com.thoughtworks.selenium.Selenium;

public interface SeleniumFactory {

    Selenium createSelenium(TestEnvironment testEnvironment);

}
