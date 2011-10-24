package sk.seges.acris.recorder.rpc.domain;

import java.util.Date;
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.sesam.domain.IDomainObject;


@Entity
@Table(name = "auditLog")
//@SequenceGenerator(name = "seqALog", sequenceName = "seq_alog", initialValue = 1)
public class AuditLog implements IDomainObject<Long> {

	private static final long serialVersionUID = -8764820720362521981L;

	@Id
//	@GeneratedValue(generator = "seqALog")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private LinkedList<Integer> event;
	
	@Column
	private String targetElement;
	
	@Column
	private Date eventTime;

	@ManyToOne(fetch=FetchType.LAZY)
	private SessionLog sessionInfo;

	public SessionLog getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionLog session) {
		this.sessionInfo = session;
	}

	public String getTargetElement() {
		return targetElement;
	}

	public void setTargetElement(String targetElement) {
		this.targetElement = targetElement;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public LinkedList<Integer> getEvent() {
		return event;
	}

	public void setEvent(int[] event) {
		LinkedList<Integer> eventList = new LinkedList<Integer>();
		
		for (int eventPart : event) {
			eventList.add(eventPart);
		}
		this.event = eventList;
	}

	public void setEvent(LinkedList<Integer> event) {
		this.event = event;
	}

	public Long getId() {
		return id;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (event != null) {
			int eventValue = 1;
			for (int ev : event) {
				eventValue += ev;
			}
			result = prime * result + eventValue;
		}
		result = prime * result
				+ ((eventTime == null) ? 0 : eventTime.hashCode());
		result = prime * result
				+ ((sessionInfo == null) ? 0 : sessionInfo.hashCode());
		result = prime * result
				+ ((targetElement == null) ? 0 : targetElement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditLog other = (AuditLog) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else {
			if (event.size() != other.event.size())
				return false;

			Long value = 0L;
			
			for (int ev : event)
				value += ev;
			for (int ev : other.event)
				value -= ev;

			if (value != 0)
				return false;
		}

		if (eventTime == null) {
			if (other.eventTime != null)
				return false;
		} else if (!eventTime.equals(other.eventTime))
			return false;
		if (sessionInfo == null) {
			if (other.sessionInfo != null)
				return false;
		} else if (!sessionInfo.equals(other.sessionInfo))
			return false;
		if (targetElement == null) {
			if (other.targetElement != null)
				return false;
		} else if (!targetElement.equals(other.targetElement))
			return false;
		return true;
	}
}