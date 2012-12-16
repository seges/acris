/**
 * 
 */
package sk.seges.corpis.domain.shared.pay.tatra;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import sk.seges.corpis.RegexConstants;
import sk.seges.corpis.domain.shared.pay.HasKeyPaymentMethodSettings;
import sk.seges.corpis.domain.shared.pay.PaymentMethodSettings;

/**
 * @author ladislav.gazo
 */
public class TatraPaySettings implements PaymentMethodSettings, HasKeyPaymentMethodSettings {
	protected static final long serialVersionUID = -8243491698232213L;

	public enum LangValues {
		sk, en, de, hu
	}
	
	public enum AutoRedir {
		YES("1"), NO("0");
		
		private String value;

		private AutoRedir(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	public static final String MID = "mid";
	
	/** Present because of ability to switch between production and sandbox. */
	@Pattern(regexp = RegexConstants.WEB_URL)
	@Column
	@NotNull
	protected String baseUrl;
	
	@Column
	@NotNull
	protected String key;
	
	@Column
	protected String pt = "TatraPay";
	
	@Size(min = 3, max = 4)
	@Column(length = 4)
	@NotNull
	protected String mid;
	
	@Pattern(regexp = RegexConstants.WEB_URL_WITHOUT_QUERY_PART)
	@Column
	@NotNull
	protected String rurl;
	
	@Size(max = 15)
	@Column(length = 15)
	protected String rsms;
	
	@Size(max = 35)
	@Column(length = 35)
	@Pattern(regexp = RegexConstants.OPTIMAL_EMAIL)
	protected String rem;
	
	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	protected AutoRedir aredir = AutoRedir.YES;
	
	@Column(length = 2)
	@Enumerated(EnumType.STRING)
	protected LangValues lang;

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
	
	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
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

	public AutoRedir getAredir() {
		return aredir;
	}

	public void setAredir(AutoRedir aredir) {
		this.aredir = aredir;
	}

	public LangValues getLang() {
		return lang;
	}

	public void setLang(LangValues lang) {
		this.lang = lang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mid == null) ? 0 : mid.hashCode());
		result = prime * result + ((pt == null) ? 0 : pt.hashCode());
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
		TatraPaySettings other = (TatraPaySettings) obj;
		if (mid == null) {
			if (other.mid != null)
				return false;
		} else if (!mid.equals(other.mid))
			return false;
		if (pt == null) {
			if (other.pt != null)
				return false;
		} else if (!pt.equals(other.pt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TatraPaySettings [aredir=" + aredir + ", baseUrl=" + baseUrl + ", key=" + key + ", lang="
				+ lang + ", mid=" + mid + ", pt=" + pt + ", rem=" + rem + ", rsms=" + rsms + ", rurl=" + rurl
				+ "]";
	}
}
