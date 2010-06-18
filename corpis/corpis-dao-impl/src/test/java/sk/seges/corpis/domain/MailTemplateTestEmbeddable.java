package sk.seges.corpis.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.ManyToOne;


@Embeddable
public class MailTemplateTestEmbeddable implements Serializable {

	private static final long serialVersionUID = -7338471264050371252L;

	private String subject;
	
	private String mailBody;
	
	@ManyToOne
	private UserTestDO toUser;
	
	@Embedded
	private CommonMailStuffTestEmbeddable commonStuff;

	public MailTemplateTestEmbeddable() {
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public UserTestDO getToUser() {
		return toUser;
	}

	public void setToUser(UserTestDO toUser) {
		this.toUser = toUser;
	}

	public CommonMailStuffTestEmbeddable getCommonStuff() {
		return commonStuff;
	}

	public void setCommonStuff(CommonMailStuffTestEmbeddable commonStuff) {
		this.commonStuff = commonStuff;
	}
	
}
