package sk.seges.svn.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import sk.seges.svn.ProjectSettings;
import sk.seges.svn.json.data.OverallInfo;
import sk.seges.svn.json.data.RevisionInfo;

public class SVNMailSender {

	private OverallInfo commitInfo;
	private ProjectSettings mailConfiguration;
	
	private static final Logger log = Logger.getLogger(SVNMailSender.class.getName());

	public SVNMailSender(ProjectSettings mailConfiguration, OverallInfo commitInfo) {
		this.commitInfo = commitInfo;
		this.mailConfiguration = mailConfiguration;
	}
	
	public void sendMail() {
		List<String> mailAddresses = mailConfiguration.getMailAddresses();
		sendMail(mailAddresses);
	}

	private void sendMail(List<String> mails) {
		String lineSeparator = "\n";

		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

		for (RevisionInfo revisionInfo : commitInfo.getRevisions()) {
			String body = "";
			body += "Repository: " + commitInfo.getRepositoryPath() + lineSeparator;
			body += "Revision: " + revisionInfo.getRevisionNr() + lineSeparator;
			body += "Author: " + revisionInfo.getAuthor() + lineSeparator;
			body += "" + lineSeparator;
			
			Date date = new Date();
			date.setTime(revisionInfo.getTimestamp() * 1000);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z (EEE, dd MMM yyyy)");
			
			body += "Date: " + dateFormat.format(date) + lineSeparator;
			body += "Log msg: " + revisionInfo.getMessage() + lineSeparator;
			body += "" + lineSeparator;
			body += "Changes:" + lineSeparator;

			for (String change : revisionInfo.getAddedPaths()) {
				body += "A " + change + lineSeparator;
			}
			for (String change : revisionInfo.getModifiedPaths()) {
				body += "U " + change + lineSeparator;
			}
			for (String change : revisionInfo.getRemovedPaths()) {
				body += "D " + change + lineSeparator;
			}

            for (String mail : mails) {
            	if (mail == null || mail.indexOf("@") == -1) {
            		continue;
            	}
				try {
		            Message msg = new MimeMessage(session);
		            msg.setFrom(new InternetAddress(mailConfiguration.getFromMail()));
		    		log.info("Sending mail to: " + mail);
			        msg.addRecipient(Message.RecipientType.TO,
			                          new InternetAddress(mail));
		            msg.setSubject("[SVN - " + commitInfo.getRepositoryPath() + "]: " + revisionInfo.getMessage());
		            msg.setText(body);
		            Transport.send(msg);
		        } catch (Exception e) {
		        	throw new RuntimeException(e);
		        }
            }
		}
	}
}