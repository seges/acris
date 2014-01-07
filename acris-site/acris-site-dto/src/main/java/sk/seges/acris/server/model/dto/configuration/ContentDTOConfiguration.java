package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.domain.server.domain.base.ContentBase;
import sk.seges.acris.node.MenuItem;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.shared.domain.api.ContentForUtils;
import sk.seges.sesam.pap.model.annotation.*;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@TransferObjectMapping(domainClass = ContentBase.class, generateConverter = false)
@Mapping(MappingType.EXPLICIT)
@Copy(annotations = @Annotations(accessor = PropertyAccessor.PROPERTY, packageOf = Size.class))
@GenerateHashcode(generate = true)
@GenerateEquals(generate = true)
public interface ContentDTOConfiguration extends IDataTransferObject, ContentForUtils {

	@TransferObjectMapping(domainClass = ContentData.class, configuration = ContentDTOConfiguration.class)
	public interface ContentDataDTOConfiguration {}

	void id();

//	@Ignore
//	void webId();
//
//	@Ignore
//	void serverParams();

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
	void params();
	void ref();
	void version();
	void label();
	void position();
	void hasChildren();
	void defaultlyLoaded();
	void token();
	void subContents();
	void decoration();
	void group();
	void parent();
	void pageName();
	void index();
	@ReadOnly
	void idForACL();
}