package sk.seges.crm.shared.domain.dto;

import java.util.Date;

/**
 * for the time being until processor is invented
 * 
 * @author ladislav.gazo
 *
 */
@Deprecated
public class LeadActivityDto {
	private String note;
	private Date when;
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getWhen() {
		return when;
	}
	public void setWhen(Date when) {
		this.when = when;
	}
	

}
