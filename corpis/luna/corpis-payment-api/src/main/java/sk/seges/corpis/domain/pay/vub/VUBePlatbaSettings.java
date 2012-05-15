package sk.seges.corpis.domain.pay.vub;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import sk.seges.corpis.RegexConstants;
import sk.seges.corpis.domain.pay.HasKeyPaymentMethodSettings;
import sk.seges.corpis.domain.pay.PaymentMethodSettings;

/**
 * @author ladislav.gazo
 */
public class VUBePlatbaSettings implements PaymentMethodSettings, HasKeyPaymentMethodSettings {
	private static final long serialVersionUID = -9212175810878072757L;

	public static final String MID = "mid";
	
	/** 
	 * Present because of ability to switch between production and sandbox. 
	 * 
	 * https://ib.vub.sk/e-platbyeuro.aspx
	 */
	@Pattern(regexp = RegexConstants.WEB_URL)
	@Column
	@NotNull
	private String baseUrl;
	
	@Column
	@NotNull
	private String key;
	
	@Size(max = 20)
	@Column(length = 20)
	@NotNull
	private String mid;
	
	@Pattern(regexp = RegexConstants.WEB_URL_WITHOUT_QUERY_PART)
	@Column
	@NotNull	
	private String rurl;
	
	@Size(max = 15)
	@Column(length = 15)
	private String rsms;
	
	@Size(max = 35)
	@Column(length = 35)
	@Pattern(regexp = RegexConstants.OPTIMAL_EMAIL)
	private String rem;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getRurl() {
		return rurl;
	}

	public void setRurl(String rurl) {
		this.rurl = rurl;
	}

	public String getRsms() {
		return rsms;
	}

	public void setRsms(String rsms) {
		this.rsms = rsms;
	}

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mid == null) ? 0 : mid.hashCode());
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
		VUBePlatbaSettings other = (VUBePlatbaSettings) obj;
		if (mid == null) {
			if (other.mid != null)
				return false;
		} else if (!mid.equals(other.mid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VUBePlatbaSettings [baseUrl=" + baseUrl + ", key=" + key + ", mid=" + mid + ", rem=" + rem
				+ ", rsms=" + rsms + ", rurl=" + rurl + "]";
	}
}
