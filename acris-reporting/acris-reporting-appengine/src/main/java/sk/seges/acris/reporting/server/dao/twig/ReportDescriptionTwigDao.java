package sk.seges.acris.reporting.server.dao.twig;

import sk.seges.acris.reporting.rpc.dao.IReportDescriptionDao;
import sk.seges.acris.reporting.rpc.domain.twig.TwigReportDescription;
import sk.seges.acris.security.server.dao.twig.AbstractTwigCrud;

import com.vercer.engine.persist.ObjectDatastore;

public class ReportDescriptionTwigDao extends AbstractTwigCrud<TwigReportDescription> implements IReportDescriptionDao<TwigReportDescription> {

	public ReportDescriptionTwigDao(ObjectDatastore datastore,
			Class<? extends TwigReportDescription> clazz) {
		super(datastore, clazz);
	}

	@Override
	public TwigReportDescription getEntityInstance() {
		return new TwigReportDescription();
	}
}
