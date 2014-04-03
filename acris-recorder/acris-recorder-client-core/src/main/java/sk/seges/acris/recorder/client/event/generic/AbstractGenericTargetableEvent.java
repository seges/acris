package sk.seges.acris.recorder.client.event.generic;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import sk.seges.acris.recorder.client.tools.CacheMap;

import java.util.Date;

public abstract class AbstractGenericTargetableEvent extends AbstractGenericEvent implements HasTargetEvent {

	protected String relatedTargetXpath;
	private final CacheMap cacheMap;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((relatedTargetXpath == null) ? 0 : relatedTargetXpath.hashCode());
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
		if (relatedTargetXpath == null) {
			if (other.relatedTargetXpath != null)
				return false;
		} else if (!relatedTargetXpath.equals(other.relatedTargetXpath))
			return false;
		return true;
	}

	protected AbstractGenericTargetableEvent(CacheMap cacheMap) {
        this.cacheMap = cacheMap;
	}
	
	protected AbstractGenericTargetableEvent(CacheMap cacheMap, Event event) {
		super(event);

        this.cacheMap = cacheMap;

        long start = new Date().getTime();

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

        GWT.log("" + (new Date().getTime() - start));
	}


	protected void initTarget(Element target, Event event) {
		this.relatedTargetXpath = cacheMap.resolveXpath(target);
	}

	public boolean hasTarget() {
		return true;
	}

	public String getRelatedTargetXpath() {
		return relatedTargetXpath;
	}

	public void setRelatedTargetXpath(String relatedTargetXpath) {
		this.relatedTargetXpath = relatedTargetXpath;
	}

	public Element getElement() {
		prepareEvent();
		return el;
	}

	@Override
	public void prepareEvent() {
		if (el == null) {
			el = cacheMap.resolveElement(getRelatedTargetXpath());
		}
	}
}
