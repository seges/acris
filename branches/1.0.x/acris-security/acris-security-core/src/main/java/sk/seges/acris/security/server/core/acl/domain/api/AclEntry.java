package sk.seges.acris.security.server.core.acl.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.binding.client.processor.DBPropertyConverter;
import sk.seges.acris.binding.client.processor.PojoPropertyConverter;
import sk.seges.sesam.domain.IDomainObject;

@BeanWrapper(beanPropertyConverter = {PojoPropertyConverter.class, DBPropertyConverter.class})
public interface AclEntry extends IDomainObject<Long> {

//    public static final String DB_SID = "sid"; 
//    public static final String A_SID_ID = "sid.id"; 
//	public static final String A_OBJECT_IDENTITY_ID = "objectIdentity.id"; 

	AclSecuredObjectIdentity getObjectIdentity();

	void setObjectIdentity(AclSecuredObjectIdentity objectIdentity);

	AclSid getSid();

	void setSid(AclSid sid);

	int getMask();

	void setMask(int mask);

	int getAceOrder();

	void setAceOrder(int aceOrder);

	boolean isGranting();

	void setGranting(boolean granting);

	boolean isAuditSuccess();

	void setAuditSuccess(boolean auditSuccess);

	boolean isAuditFailure();

	void setAuditFailure(boolean auditFailure);
}