package sk.seges.acris.recorder.client.event.generic;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

public abstract class AbstractGenericTargetableEvent extends AbstractGenericEvent implements HasTargetEvent {

	protected String relatedTargetXpath;
	protected final ElementXpathCache elementXpathCache;

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

	protected AbstractGenericTargetableEvent(ElementXpathCache elementXpathCache) {
        this.elementXpathCache = elementXpathCache;
	}
	
	protected AbstractGenericTargetableEvent(ElementXpathCache elementXpathCache, Event event) {
		super(event);

        this.elementXpathCache = elementXpathCache;

		Element target = DOM.eventGetTarget(event);

		if (target != null) {
		    // handle click events
			initTarget(target, event);
		} else {
            // Last event
            target = DOM.eventGetCurrentTarget(event);
			if (target != null) {
				initTarget(target, event);
			} else {
                // Mouse out event from
				target = DOM.eventGetFromElement(event);

				if (target != null) {
					initTarget(target, event);
				} else {
                    // Mouse over event to
					target = DOM.eventGetToElement(event);

					if (target != null) {
						initTarget(target, event);
					} else {
                        // Unable to identify element, so it'll be null
					}
				}
			}
		}
	}

	protected void initTarget(Element target, Event event) {
		this.relatedTargetXpath = elementXpathCache.resolveXpath(target);
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
			el = elementXpathCache.resolveElement(getRelatedTargetXpath());
		}
	}
}