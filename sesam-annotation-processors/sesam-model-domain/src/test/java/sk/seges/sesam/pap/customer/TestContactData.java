/**
 * 
 */
package sk.seges.sesam.pap.customer;

import java.io.Serializable;


/**
 * @author ladislav.gazo
 */
public interface TestContactData extends Serializable {
	public static final String PHONE = "phone";
	public static final String FAX = "fax";
	public static final String EMAIL = "email";
	public static final String MOBILE = "mobile";
	public static final String WEB = "web";

	String getPhone();
	void setPhone(String phone);
	
	String getFax();
	void setFax(String fax);
	
	String getEmail();
	void setEmail(String email);
	
	String getMobile();
	void setMobile(String mobile);
	
	String getWeb();
	void setWeb(String web);
}
