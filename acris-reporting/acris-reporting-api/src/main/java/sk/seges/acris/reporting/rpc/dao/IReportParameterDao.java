package sk.seges.acris.reporting.rpc.dao;

import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.IEntityInstancer;

/**
 * dao interface for ReportParameterData
 * @author marta
 *
 * @param <T> extends {@link ReportParameterData}
 */
@Deprecated
public interface IReportParameterDao<T extends ReportParameterData> extends ICrudDAO<T>, IEntityInstancer<T> {
}