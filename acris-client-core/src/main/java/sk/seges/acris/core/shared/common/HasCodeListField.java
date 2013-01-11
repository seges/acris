package sk.seges.acris.core.shared.common;

import sk.seges.corpis.server.domain.HasWebId;

public interface HasCodeListField extends HasWebId {

	int getPosition();
	
	void setPosition(int position);
	
	/**
	 * localized names: {"locale":"localized name","locale_2":"localized_name"}
	 */
	String getNames();
	
	void setNames(String names);
}
