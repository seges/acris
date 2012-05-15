package sk.seges.corpis.core.pap.dao.configurer;


public class DaoApiProcessorConfigurer extends AbstractDaoApiConfigurer {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/dao-api.properties";

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}
	
}