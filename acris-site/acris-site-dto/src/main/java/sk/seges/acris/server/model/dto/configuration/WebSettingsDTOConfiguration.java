/**
 * Copyright (c) 2012 Seges s.r.o.
 * All Rights Reserved.
 *
 * Any usage, duplication or redistribution of this software is allowed only
 * according to separate agreement prepared in written between Seges
 * and authorized party.
 */
package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

/**
 * @author psenicka
 *
 */
@TransferObjectMapping(domainClass = JpaWebSettings.class)
@Mapping(Mapping.MappingType.EXPLICIT)
@GenerateHashcode(generate = false)
@GenerateEquals(generate = false)
public interface WebSettingsDTOConfiguration {

	void id();
	void parameters();
	void stockCountdown();
	void thumbnailMaxHeight();
	void thumbnailMaxWidth();
	void metaData();
	void imageMaxHeight();
	void imageMaxWidth();
	void stockAmountForWhichTheProductDisplay();
	void termOfPayment();
	void constantSymbol();
	void analyticsScriptData();
}
