package sk.seges.sesam.pap.test.selenium.processor.runner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.test.selenium.configuration.annotation.CredentialsSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.MailSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;

public class DefaultRunner {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		new DefaultRunner().printHelp(args);
	}
	
	protected void printHelp(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		
		String mainClass = System.getProperty("exec.mainClass");
		
		if (mainClass != null) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(mainClass);
			} catch (Exception e) {
				System.out.println("[ERROR] Unable to find main class " + mainClass);
			}
			
			if (clazz != null) {
				Class<?>[] argTypes = { args.getClass(), };
		        Object[] passedArgs = { args };
		        Method main = clazz.getMethod("main",argTypes);
		        main.invoke(null, passedArgs);
		        return;
			}
		}
		
		ConfigurationValue[] configurationValues = new ConfigurationValue[] {};
		
		new SeleniumSettings(configurationValues).printHelp(System.out);
		new ReportSettings(configurationValues).printHelp(System.out);
		new MailSettings(configurationValues).printHelp(System.out);
		new CredentialsSettings(configurationValues).printHelp(System.out);
	}
}