/**
 * Copyright (c) 2012 Seges s.r.o.
 * All Rights Reserved.
 *
 * Any usage, duplication or redistribution of this software is allowed only
 * according to separate agreement prepared in written between Seges
 * and authorized party.
 */
package sk.seges.acris.server.model.dto.configuration;

import sk.seges.corpis.server.domain.jpa.JpaCountry;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaCountry.class)
public interface CountryDTOConfiguration {}