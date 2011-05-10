package sk.seges.sesam.core.test.selenium.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import sk.seges.sesam.core.test.selenium.configuration.api.MailSettings;
import sk.seges.sesam.core.test.selenium.support.api.MailSupport;

public class DefaultMailSupport implements MailSupport {

	private MailSettings mailEnvironment;
	
	public DefaultMailSupport(MailSettings mailEnvironment) {
		this.mailEnvironment = mailEnvironment;
	}

	protected void verifyMailSettings() {
		assert mailEnvironment != null;

		assert mailEnvironment.getHost() != null;
		assert mailEnvironment.getMail() != null;
		assert mailEnvironment.getPassword() != null;
	}
	
	public void waitForMailNotPresent(String subject) throws InterruptedException {

		verifyMailSettings();
		
		for (int tries = 0;; tries++) {
			if (tries >= 10) {
				break;
			}
			try {
				if (containsMail(subject) != null) {
					throw new RuntimeException("Mail with subject: " + subject + " did arrived into the mailbox.");
				}
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
	}

	public String waitForMailPresent(String subject) throws InterruptedException {

		verifyMailSettings();

		for (int tries = 0;; tries++) {
			if (tries >= 10) {
				throw new RuntimeException("Mail with subject: " + subject + " did not arrived into the mailbox in specified time interval.");
			}
			try {
				String content = containsMail(subject);
				
				if (content != null) {
					return content;
				}
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
	}

	protected String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		}
		
		return "";
	}

	protected String toString(Multipart content) throws MessagingException, IOException {
		String result = "";
		
		Multipart mp = (Multipart) content;
		int count = mp.getCount();

		for (int j = 0; j < count; j++) {

			BodyPart part = mp.getBodyPart(j);

			Object partContent = part.getContent();

			if (partContent instanceof String) {
				result += (String) partContent;
			} else if (partContent instanceof Multipart) {
				result += toString((Multipart)partContent);
			} else if (partContent instanceof InputStream) {
				result += convertStreamToString((InputStream) partContent);
			}
		}
		
		return result;
	}
	
	protected String getMailContent(Message m) throws MessagingException, IOException {

		Object content = m.getContent();

		if (content instanceof String) {
			return (String) content;
		}

		if (content instanceof Multipart) {
			return toString((Multipart) content);
		} else if (content instanceof InputStream) {
			return convertStreamToString((InputStream) content);
		}

		return null;
	}

	protected String containsMail(String subject) {
		
		if (subject == null || subject.length() == 0) {
			throw new RuntimeException("Invalid subject. Please, enter valid mail subject!");
		}

		Properties props = new Properties();

//		props.setProperty("mail.store.protocol", "imaps");
		
		String result = null;
		
		try {
			// Connect to the server
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore(mailEnvironment.getProvider().toString());
			store.connect(mailEnvironment.getHost(), mailEnvironment.getMail(), mailEnvironment.getPassword());

			// open the inbox folder
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			// get a list of javamail messages as an array of messages
			Message[] messages = inbox.getMessages();

			for (int i = 0; i < messages.length; i++) {
				if (!messages[i].getFlags().contains(Flag.DELETED)) {
					String mailSubject = messages[i].getSubject();
	
					if (mailSubject != null && mailSubject.contains(subject)) {
						result = getMailContent(messages[i]);
						messages[i].setFlag(Flags.Flag.DELETED, true);
						break;
					}
				}
			}

			// close the inbox folder but do not
			// remove the messages from the server
			inbox.close(false);
			store.close();
		} catch (NoSuchProviderException nspe) {
			System.err.println("invalid provider name");
		} catch (MessagingException me) {
			System.err.println("messaging exception");
			me.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO Exception");
			e.printStackTrace();
		}
		
		return result;
	}
}