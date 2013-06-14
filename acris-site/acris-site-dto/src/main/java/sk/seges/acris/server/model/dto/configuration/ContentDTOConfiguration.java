package sk.seges.acris.server.model.dto.configuration;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.domain.server.domain.base.ContentBase;
import sk.seges.acris.node.MenuItem;
import sk.seges.acris.site.server.domain.api.ContentPkData;
import sk.seges.sesam.pap.model.annotation.Annotations;
import sk.seges.sesam.pap.model.annotation.Copy;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.PropertyAccessor;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.shared.domain.api.HasId;

@TransferObjectMapping(domainClass = ContentBase.class, generateConverter = false)
@Mapping(MappingType.AUTOMATIC)
@Copy(annotations = @Annotations(accessor = PropertyAccessor.PROPERTY, packageOf = Size.class))
public interface ContentDTOConfiguration extends IDataTransferObject, HasId<ContentPkData> {

	@Ignore
	void webId();
	
	@Ignore
	void serverParams();

	@NotNull
	@Size(min = 1, max = 255)
	@Pattern(regexp = "[a-zA-Z0-9\\-_/]*")
	void niceUrl();

	@Size(min = 0, max = 255)
	void description();

	@Size(min = 0, max = 255)
	void keywords();

	@Size(min = 0, max = 255)
	void title();

	@Field("content")
	String contentDetached();

	@Field("menuItems")
	MenuItem menuItemsData();

	Date created();

	Date modified();
}