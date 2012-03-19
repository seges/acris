package sk.seges.corpis.server.domain.invoice.base;

import sk.seges.corpis.shared.domain.invoice.api.CurrencyData;

public class CurrencyBase implements CurrencyData {

	private static final long serialVersionUID = -1055660478821033276L;

	private Short id;
	
	/**
	 * 3-letter code as defined in ISO 4217
	 */
	private String code;

	@Override
	public Short getId() {
		return id;
	}

	@Override
	public void setId(Short id) {
		this.id = id;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		CurrencyData other = (CurrencyData) obj;
		if (code == null) {
			if (other.getCode() != null)
				return false;
		} else if (!code.equals(other.getCode()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Currency [code=" + code + ", id=" + id + "]";
	}

}
