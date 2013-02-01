package sk.seges.acris.generator.server.spring.configuration;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.action.GenerateOfflineActionHandler;
import sk.seges.acris.generator.server.action.GetAvailableNiceurlsActionHandler;
import sk.seges.acris.generator.server.action.GetDefaultGeneratorTokenActionHandler;
import sk.seges.acris.generator.server.action.ReadHtmlBodyFromFileActionHandler;
import sk.seges.acris.generator.server.action.WriteOfflineContentHtmlActionHandler;
import sk.seges.acris.generator.server.dao.IFileDao;
import sk.seges.acris.generator.server.dao.twig.TwigFileDao;
import sk.seges.acris.generator.server.domain.api.FileData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MoviesContentProvider;
import sk.seges.acris.generator.server.processor.factory.CommaSeparatedParameterManagerFactory;
import sk.seges.acris.generator.server.processor.factory.DefaultNodeParserFactory;
import sk.seges.acris.generator.server.processor.factory.HtmlProcessorFactory;
import sk.seges.acris.generator.server.processor.factory.PlainOfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.PostProcessorActivatorFactory;
import sk.seges.acris.generator.server.processor.factory.api.NodeParserFactory;
import sk.seges.acris.generator.server.processor.factory.api.OfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.generator.server.processor.mock.MockWebSettingsService;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.service.GeneratorService;
import sk.seges.acris.generator.server.service.persist.api.DataPersister;
import sk.seges.acris.generator.server.service.persist.db.DatabasePersister;
import sk.seges.acris.generator.shared.action.GenerateOfflineAction;
import sk.seges.acris.generator.shared.action.GetAvailableNiceurlsAction;

import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.server.spring.DispatchModule;
import com.gwtplatform.dispatch.server.spring.HandlerModule;
import com.gwtplatform.dispatch.server.spring.actionvalidator.DefaultActionValidator;
import com.gwtplatform.dispatch.server.spring.configuration.DefaultModule;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

@Import({DefaultModule.class, DispatchModule.class, PostProcessorsConfiguration.class})
public class ActionModule extends HandlerModule {

	private static final String INDEX_FILE = "index.html";

	public ActionModule() {
	}

	@Bean
	public GenerateOfflineActionHandler generateOfflineActionHandler() {
		return new GenerateOfflineActionHandler();
	}

	@Bean
	public GetAvailableNiceurlsActionHandler getAvailableNiceurlsActionHandler() {
		return new GetAvailableNiceurlsActionHandler();
	}

	@Bean
	public GetDefaultGeneratorTokenActionHandler getLastProcessingTokenActionHandler() {
		return new GetDefaultGeneratorTokenActionHandler();
	}

	@Bean
	public WriteOfflineContentHtmlActionHandler getOfflineContentHtmlActionHandler() {
		return new WriteOfflineContentHtmlActionHandler(generatorService());
	}

	@Bean
	public ReadHtmlBodyFromFileActionHandler readHtmlBodyFromFileActionHandler() {
		return new ReadHtmlBodyFromFileActionHandler(generatorService());
	}

	@Bean
	public ObjectDatastore objectDatastore() {
		return new AnnotationObjectDatastore();
	}

	@Bean
	public IFileDao<FileData> fileDao() {
		return new TwigFileDao(objectDatastore());
	}

	@Bean
	public DataPersister dataPersister() {
		return new DatabasePersister(fileDao());
	}

	@Bean
	public ContentDataProvider contentDataProvider() {
		return new MoviesContentProvider();
	}

	@Bean
	public ParametersManagerFactory parametersManagerFactory() {
		return new CommaSeparatedParameterManagerFactory();
	}
	
	@Bean
	public OfflineWebSettingsFactory offlineWebSettingsFactory() {
		return new PlainOfflineWebSettingsFactory(parametersManagerFactory());
	}
	
	@Bean
	public PostProcessorActivatorFactory postProcessorActivatorFactory() {
		return new PostProcessorActivatorFactory(offlineWebSettingsFactory());
	}
	
	@Bean
	public NodeParserFactory parserFactory() {
		return new DefaultNodeParserFactory();
	}
	
	@Bean
	public HtmlProcessorFactory getHtmlProcessorFactory() {
		Map<String, AbstractElementPostProcessor> abstractPostProcessors = this.applicationContext.getBeansOfType(AbstractElementPostProcessor.class);
		return new HtmlProcessorFactory(abstractPostProcessors.values(), postProcessorActivatorFactory(), contentDataProvider(), parserFactory());
	}
	
	@Bean
	public GeneratorService generatorService() {
		return new GeneratorService(dataPersister(), INDEX_FILE, contentDataProvider(),
				new MockWebSettingsService(), getHtmlProcessorFactory(), parserFactory());
	}

	@Bean
	public ActionValidator getDefaultActionValidator() {
		return new DefaultActionValidator();
	}

	protected void configureHandlers() {
		bindHandler(GenerateOfflineAction.class, GenerateOfflineActionHandler.class);
		bindHandler(GetAvailableNiceurlsAction.class, GetAvailableNiceurlsActionHandler.class);
	}
}