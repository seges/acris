package sk.seges.acris.reporting.server.dao.api;

import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.IEntityInstancer;

/**
 * dao interface for ReportParameterData
 * @author marta
 *
 * @param <T> extends {@link ReportParameterData}
 */
public interface IReportParameterDao<T extends ReportParameterData> extends ICrudDAO<T>, IEntityInstancer<T> {
}