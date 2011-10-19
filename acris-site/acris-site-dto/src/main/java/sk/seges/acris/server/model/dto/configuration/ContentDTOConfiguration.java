package sk.seges.acris.server.model.dto.configuration;

import java.util.Date;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.domain.server.domain.base.ContentBase;
import sk.seges.acris.node.MenuItem;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Id;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = ContentBase.class)
@Mapping(MappingType.AUTOMATIC)
public interface ContentDTOConfiguration extends IDataTransferObject {

	@Ignore
	void webId();

	@Id
	void id();

	@Field("content")
	String contentDetached();

	@Field("menuItems")
	MenuItem menuItemsData();

	Date created();

	Date modified();
}