/**
 * 
 */
package sk.seges.corpis.shared.domain.customer.base;

import sk.seges.corpis.shared.domain.customer.api.BasicContactData;


/**
 * @author ladislav.gazo
 */
public class BasicContactBase implements BasicContactData { 
	private static final long serialVersionUID = 1469865350937301761L;
	
	protected String phone;
    protected String fax;
    protected String email;
    protected String mobile;
    protected String web;
	
	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public String getMobile() {
		return mobile;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public String getWeb() {
		return web;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Override
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public void setWeb(String web) {
		this.web = web;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((web == null) ? 0 : web.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicContactBase other = (BasicContactBase) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (web == null) {
			if (other.web != null)
				return false;
		} else if (!web.equals(other.web))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "BasicContactDto [email=" + email + ", fax=" + fax + ", mobile=" + mobile + ", phone=" + phone
				+ ", web=" + web + "]";
	}

}
