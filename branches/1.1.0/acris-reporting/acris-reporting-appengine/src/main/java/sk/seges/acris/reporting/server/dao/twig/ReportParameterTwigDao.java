package sk.seges.acris.reporting.server.dao.twig;

import sk.seges.acris.reporting.rpc.dao.IReportParameterDao;
import sk.seges.acris.reporting.rpc.domain.twig.TwigReportParameter;
import sk.seges.acris.security.server.dao.twig.AbstractTwigCrud;

import com.vercer.engine.persist.ObjectDatastore;

/**
 * twig implementation of IReportParameterDao
 * @author marta
 *
 */
public class ReportParameterTwigDao extends AbstractTwigCrud<TwigReportParameter> implements IReportParameterDao<TwigReportParameter> {

	public ReportParameterTwigDao(ObjectDatastore datastore,
			Class<? extends TwigReportParameter> clazz) {
		super(datastore, clazz);
	}

	@Override
	public TwigReportParameter getEntityInstance() {
		return new TwigReportParameter();
	}

}
