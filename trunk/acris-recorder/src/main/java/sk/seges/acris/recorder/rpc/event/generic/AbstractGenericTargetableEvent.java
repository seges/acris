package sk.seges.acris.recorder.rpc.event.generic;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public abstract class AbstractGenericTargetableEvent extends AbstractGenericEvent implements HasTargetEvent {

	public static final String TARGET_ATTRIBUTE = "relatedTargetId";
	
	protected String relatedTargetId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((relatedTargetId == null) ? 0 : relatedTargetId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof AbstractGenericTargetableEvent))
			return false;
		AbstractGenericTargetableEvent other = (AbstractGenericTargetableEvent) obj;
		if (relatedTargetId == null) {
			if (other.relatedTargetId != null)
				return false;
		} else if (!relatedTargetId.equals(other.relatedTargetId))
			return false;
		return true;
	}

	protected AbstractGenericTargetableEvent() {
	}
	
	protected AbstractGenericTargetableEvent(Event event) {
		super(event);
		
		Element target = DOM.eventGetTarget(event);

		if (target != null) { // handle click events
			initTarget(target, event);
		} else {
			target = DOM.eventGetCurrentTarget(event); // Last event
			if (target != null) {
				initTarget(target, event);
			} else {
				target = DOM.eventGetFromElement(event); // Mouse out event from

				if (target != null) {
					initTarget(target, event);
				} else {
					target = DOM.eventGetToElement(event); // Mouse over event
															// to

					if (target != null) {
						initTarget(target, event);
					} else {
						// TODO
					}
				}
			}
		}
	}
	
	protected void initTarget(Element target, Event event) {
		this.relatedTargetId = target.getAttribute(ELEMENT_ID_NAME);
		GWT.log(target.getString(), null);
		GWT.log(this.relatedTargetId, null);
	}

	public boolean hasTarget() {
		return true;
	}

	public String getRelatedTargetId() {
		return relatedTargetId;
	}

	public void setRelatedTargetId(String relatedTargetId) {
		this.relatedTargetId = relatedTargetId;
	}

//	public void asd() { 
//		Document.get().getElementsByTagName(tagName)
//	}
	
	public void prepareEvent() {
		if (el == null) {
			if (cacheMap != null) {
				el = cacheMap.resolveElement(relatedTargetId);
			} else {
				if (relatedTargetId != null && relatedTargetId.length() > 0) {
//					GQuery gquery = $("*[elementID=" + relatedTargetId + "]");
//					el = gquery.get(0)
//							.<com.google.gwt.user.client.Element> cast();
				} else {
					el = Document.get().getDocumentElement()
							.<com.google.gwt.user.client.Element> cast();
				}
			}
		}
	}
}
