package sk.seges.acris.domain.shared.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.corpis.shared.domain.api.HasLanguage;
import sk.seges.corpis.shared.domain.api.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@BeanWrapper
@MetaModel
public interface ContentPkData extends IMutableDomainObject<Long>, HasWebId, HasLanguage {}