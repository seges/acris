package sk.seges.corpis.server.domain.invoice.base;

import sk.seges.corpis.shared.domain.invoice.api.UnitData;
import sk.seges.corpis.shared.domain.invoice.api.UnitType;

@SuppressWarnings("serial")
public abstract class UnitBase<K> implements UnitData<K> {

	public static final String LABEL_KEY = "labelKey";
	public static final String TYPE = "type";

	private String labelKey;
	private UnitType type;

	@Override
	public String getLabelKey() {
		return labelKey;
	}

	@Override
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}

	@Override
	public UnitType getType() {
		return type;
	}

	@Override
	public void setType(UnitType type) {
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((labelKey == null) ? 0 : labelKey.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		UnitBase<?> other = (UnitBase<?>) obj;
		if (labelKey == null) {
			if (other.labelKey != null)
				return false;
		} else if (!labelKey.equals(other.labelKey))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UnitBase [id=" + getId() + ", labelKey=" + labelKey + ", type=" + type + "]";
	}

}