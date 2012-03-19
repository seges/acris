package sk.seges.sesam.pap.customer;

import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
public class TestCustomer extends TestCustomerBaseCore<Integer> {
	private static final long serialVersionUID = -8991123426042992980L;

	public static final String USER_ACCOUNTS = "userAccounts";
	public static final String WEB_ID = "webId";

	private String webId;

	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

}
