Jasperserver installation:
1. download jasperserver-ce-bin with war file and buildomatic scripts, e.g. from
http://sourceforge.net/projects/jasperserver/files/JasperServer/JasperServer%203.7.0/jasperserver-ce-3.7.0-bin.zip/download
and unpack it
2. read install doc :)
3. go to js-install-dir/buildomatic and create default_master.properties, by copying it from hr-oddelenie-reporting bundle
if you need more properties, you can check
js-install-dir/buildomatic/sample_conf/inst-postgresql_master.properties (or inst-mysql_master.properties)
4. from js-install-dir/buildomatic run following commands:
	js-ant clean-config
	js-ant gen-config
	js-ant create-js-db
	js-ant init-js-db-ce
	js-ant import-minimal-ce
	js-ant deploy-webapp-ce

5. now you should be able to startup tomcat without exceptions with deployed webapp named reporting
6. admin username and password is jasperadmin
7. create user with USER_ROLE for viewing reports in your webapp

After installing jasperserver to {jaspersever-inst-dir} put:
1. applicationContext-xx.xml to {jaspersever-inst-dir}/WEB-INF
2. jars from lib folder to {jaspersever-inst-dir}/WEB-INF/lib
3. to tomcat's JAVA_OPTS add  
-Xms128m -Xmx512m -XX:PermSize=32m
-XX:MaxPermSize=128m -Xss2m -XX:+UseConcMarkSweepGC
-XX:+CMSClassUnloadingEnabled