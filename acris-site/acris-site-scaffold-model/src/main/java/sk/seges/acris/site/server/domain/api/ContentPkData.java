package sk.seges.acris.site.server.domain.api;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.corpis.server.domain.HasLanguage;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@BeanWrapper
@MetaModel
public interface ContentPkData extends IMutableDomainObject<Long>, HasWebId, HasLanguage {}