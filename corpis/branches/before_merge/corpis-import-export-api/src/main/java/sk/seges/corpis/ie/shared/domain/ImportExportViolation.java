/**
 * 
 */
package sk.seges.corpis.ie.shared.domain;

import java.io.Serializable;

/**
 * @author ladislav.gazo
 */
public class ImportExportViolation implements Serializable {
	private static final long serialVersionUID = 6072991290209787890L;

	public enum Level {
		WARN, ERROR
	}
	
	public static final int NO_ROW = -1;
	
	private Level level;
	private int row;
	private String type;
	private String value;
	
	public ImportExportViolation() {
	}
	
	public ImportExportViolation(String type) {
		this(Level.ERROR, NO_ROW, type, null);
	}
	
	public ImportExportViolation(String type, String value) {
		this(Level.ERROR, NO_ROW, type, value);
	}
	
	public ImportExportViolation(int row, String type, String value) {
		this(Level.ERROR, row, type, value);
	}
	
	public ImportExportViolation(Level level, int row, String type, String value) {
		super();
		this.level = level;
		this.row = row;
		this.type = type;
		this.value = value;
	}
	
	public Level getLevel() {
		return level;
	}

	public int getRow() {
		return row;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ImportExportViolation [level=" + level + ", row=" + row + ", type=" + type + ", value="
				+ value + "]";
	}
}
