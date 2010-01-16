package sk.seges.acris.security.server.service;

import javax.sql.DataSource;

import org.springframework.security.acls.jdbc.AclCache;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;

public class MysqlJdbcMutableAclService extends JdbcMutableAclService {
	public MysqlJdbcMutableAclService(DataSource dataSource,
			LookupStrategy lookupStrategy, AclCache aclCache) {
		super(dataSource, lookupStrategy, aclCache);
		setSidIdentityQuery("SELECT LAST_INSERT_ID()");
		setClassIdentityQuery("SELECT LAST_INSERT_ID()");
	}
}
