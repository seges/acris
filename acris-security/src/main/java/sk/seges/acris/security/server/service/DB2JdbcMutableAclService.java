package sk.seges.acris.security.server.service;

import javax.sql.DataSource;

import org.springframework.security.acls.jdbc.AclCache;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;

public class DB2JdbcMutableAclService extends JdbcMutableAclService {
	public DB2JdbcMutableAclService(DataSource dataSource,
			LookupStrategy lookupStrategy, AclCache aclCache) {
		super(dataSource, lookupStrategy, aclCache);
		setSidIdentityQuery("SELECT PREVVAL FOR ACL_SEQ_SID FROM ACL_SID");
		setClassIdentityQuery("SELECT PREVVAL FOR ACL_SEQ_CLASS FROM ACL_CLASS");
	}
}
