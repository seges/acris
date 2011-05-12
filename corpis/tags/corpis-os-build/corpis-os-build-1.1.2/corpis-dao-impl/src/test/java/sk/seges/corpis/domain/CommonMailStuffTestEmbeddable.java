package sk.seges.corpis.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class CommonMailStuffTestEmbeddable implements Serializable {
	
	private static final long serialVersionUID = -6898520967824660627L;

	String simpleMailAttribute;
	
	@ManyToOne
	LocationTestDO mailAddress;

	public String getSimpleMailAttribute() {
		return simpleMailAttribute;
	}

	public void setSimpleMailAttribute(String basicTypeMailAttribute) {
		this.simpleMailAttribute = basicTypeMailAttribute;
	}

	public LocationTestDO getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(LocationTestDO location) {
		this.mailAddress = location;
	}
}
