package sk.seges.svn.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

public class MailConfiguration {
	private List<String> mailAddress = new ArrayList<String>();
	private String fromMail;
		 
	public MailConfiguration(ServletContext context) {
		
		Properties properties;
		try {
			properties = new Properties();
			URL url = context.getResource("/WEB-INF/settings.properties");
			properties.load(new FileInputStream(new File(url.getFile())));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		fromMail = (String)properties.get("from");

		String to = (String)properties.get("to");
		
		String[] mails = to.split(";");
		
		for (String mail: mails) {
			mailAddress.add(mail);
		}
	}
	
	public List<String> getMailAddresses() {
		return mailAddress;
	}
	
	public String getFromMail() {
		return fromMail;
	}
}