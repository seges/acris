package sk.seges.acris.reporting.server.datasource;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;

import com.jaspersoft.jasperserver.api.metadata.jasperreports.service.ReportDataSourceService;


/**
 * 
 * general data source class used for JavaBeanDataSources <br />
 * list of CustomDataSource objects is used as
 * {@link JRParameter#REPORT_DATA_SOURCE} in implementations of
 * {@link ReportDataSourceService}, which are necessary imputs for JasperServer Bean DataSource
 * 
 * @author marta
 * 
 * @param <T>
 *            usually a type of object which should be displayed as row in
 *            report
 */
public class CustomDataSource<T> implements JRDataSource {

	private Collection<T> rows;
	private Iterator<T> iteratorOfRows;
	private T actualRow;

	public CustomDataSource() {}
	
	public CustomDataSource(Collection<T> rows) {
		super();
		init(rows);
	}

	public void init(Collection<T> rows) {
		if (rows != null) {
			this.rows = rows;
			iteratorOfRows = this.rows.iterator();
		}
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		if (actualRow == null)
			return null;
		Object value = null;
		String fieldName = jrField.getName();
		Class<T> clazz = (Class<T>) actualRow.getClass();
		value = getFieldValueRecursive(clazz, fieldName);
		return value;
	}

	private Object getFieldValueRecursive(Class<?> clazz, String fieldName) throws JRException {
		Object value = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				try {
					field.setAccessible(true);
					value = field.get(actualRow);
				} catch (Exception e) {
					throw new JRException(e);
				}
				return value;
			}
		}
		if (clazz.getSuperclass() != null) {
			return getFieldValueRecursive(clazz.getSuperclass(), fieldName);
		}

		return null;
	}

	@Override
	public boolean next() throws JRException {
		if (iteratorOfRows == null) {
			if (rows != null)
				iteratorOfRows = rows.iterator();
			else
				return false;
		}
		if (iteratorOfRows.hasNext()) {
			actualRow = iteratorOfRows.next();
			return true;
		}
		//if it is at the end, we reset it to have it prepared for next time, but we return false, because end was achieved
		if (rows != null)
			iteratorOfRows = rows.iterator();
		return false;
	}

	public Collection<T> getRows() {
		return rows;
	}

	public void setRows(Collection<T> rows) {
		this.rows = rows;
	}

}
