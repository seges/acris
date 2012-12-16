package sk.seges.corpis.domain.shared.pay.vub;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import sk.seges.corpis.domain.shared.pay.PaymentMethodSettings;
import sk.seges.corpis.domain.shared.pay.SlovakPaymentMethodRequest;

/**
 * @author ladislav.gazo
 */
public class VUBePlatbaRequest implements SlovakPaymentMethodRequest {
	private static final long serialVersionUID = -4413530913701110648L;

	@DecimalMax(value = "9999999999")
	@NotNull
	private Long vs;
	
	@DecimalMax(value = "9999999999")
	private Long ss;
	
	@DecimalMax(value = "9999")
	private Short cs;
	
	@Digits(integer = 13, fraction = 2)
	@Column
	@NotNull
	private BigDecimal amt;
	
	@Size(max = 512)
	@Column(length = 512)
	@Pattern(regexp = "[A-Z\\d]+")
	@NotNull
	private String sign;
	
	@Size(max = 20)
	@Column(length = 20)	
	private String desc;
	
	@Valid
	private VUBePlatbaSettings settings;

	public Long getVs() {
		return vs;
	}

	@Override
	public void setVs(Long vs) {
		this.vs = vs;
	}

	public Long getSs() {
		return ss;
	}

	public void setSs(Long ss) {
		this.ss = ss;
	}

	public Short getCs() {
		return cs;
	}

	public void setCs(Short cs) {
		this.cs = cs;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	@Override
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public VUBePlatbaSettings getSettings() {
		return settings;
	}

	public void setSettings(VUBePlatbaSettings settings) {
		this.settings = settings;
	}
	
	@Override
	public void setSettings(PaymentMethodSettings settings) {
		assert settings instanceof VUBePlatbaSettings;
		this.settings = (VUBePlatbaSettings) settings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amt == null) ? 0 : amt.hashCode());
		result = prime * result + ((settings == null) ? 0 : settings.hashCode());
		result = prime * result + ((sign == null) ? 0 : sign.hashCode());
		result = prime * result + ((vs == null) ? 0 : vs.hashCode());
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
		VUBePlatbaRequest other = (VUBePlatbaRequest) obj;
		if (amt == null) {
			if (other.amt != null)
				return false;
		} else if (!amt.equals(other.amt))
			return false;
		if (settings == null) {
			if (other.settings != null)
				return false;
		} else if (!settings.equals(other.settings))
			return false;
		if (sign == null) {
			if (other.sign != null)
				return false;
		} else if (!sign.equals(other.sign))
			return false;
		if (vs == null) {
			if (other.vs != null)
				return false;
		} else if (!vs.equals(other.vs))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VUBePlatbaRequest [amt=" + amt + ", cs=" + cs + ", desc=" + desc + ", settings=" + settings
				+ ", sign=" + sign + ", ss=" + ss + ", vs=" + vs + "]";
	}
}

