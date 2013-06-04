package sk.seges.acris.reporting.server.dao.twig;

import sk.seges.acris.reporting.rpc.domain.twig.TwigReportDescription;
import sk.seges.acris.reporting.server.dao.api.IReportDescriptionDao;
import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.security.server.dao.twig.AbstractTwigCrud;

import com.vercer.engine.persist.ObjectDatastore;

public class ReportDescriptionTwigDao extends AbstractTwigCrud<ReportDescriptionData> implements IReportDescriptionDao<ReportDescriptionData> {

	public ReportDescriptionTwigDao(ObjectDatastore datastore,
			Class<? extends TwigReportDescription> clazz) {
		super(datastore, clazz);
	}

	@Override
	public TwigReportDescription getEntityInstance() {
		return new TwigReportDescription();
	}
	
	@Override
	public ReportDescriptionData persist(ReportDescriptionData entity) {
		entity.setId(System.currentTimeMillis());
		return super.persist(entity);
	}
}
