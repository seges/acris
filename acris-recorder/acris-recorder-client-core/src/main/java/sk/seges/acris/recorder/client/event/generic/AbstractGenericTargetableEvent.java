package sk.seges.acris.recorder.client.event.generic;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import sk.seges.acris.recorder.client.tools.CacheMap;

public abstract class AbstractGenericTargetableEvent extends AbstractGenericEvent implements HasTargetEvent {

	protected String relatedTargetXpath;
	private CacheMap cacheMap;

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

	public final boolean containsMoreDigits(String s){

		if (s == null || s.isEmpty()) {
			return false;
		}

		int digitCount = 0;
		for (char c : s.toCharArray()) {
			if (Character.isDigit(c)) {
				digitCount++;
				if (digitCount > 3) {
					return true;
				}
			}
		}

		return false;
	}
	protected boolean hasId(com.google.gwt.dom.client.Element element) {
		//when it contains more that 4 digits its probably generated
		return (element != null && element.getId() != null && element.getId().length() > 0 && !containsMoreDigits(element.getId()));
	}

	protected String getXPathForId(com.google.gwt.dom.client.Element element) {
		return "//*[@id='" + element.getId() + "']";
	}

	protected String getXPath(com.google.gwt.dom.client.Element element) {
		if (hasId(element)) {
			return getXPathForId(element);
		}
		return getElementTreeXpath(element);
	}

	protected String getElementTreeXpath(com.google.gwt.dom.client.Element element) {
		String result = "";

		// Use nodeName (instead of localName) so namespace prefix is included (if any).
		for (; element != null && element.getNodeType() == 1; element = element.getParentElement()) {

			if (result.length() > 0) {
				result = "/" + result;
			}

			if (hasId(element)) {
				return getXPathForId(element) + result;
			}

			int index = 0;
			for (Node sibling = element.getPreviousSibling(); sibling != null; sibling = sibling.getPreviousSibling())
			{
				// Ignore document type declaration.
				if (sibling.getNodeType() == Node.DOCUMENT_NODE)
					continue;

				if (sibling.getNodeName().equals(element.getNodeName())) {
					++index;
				}
			}

			String tagName = element.getNodeName().toLowerCase();
			String pathIndex = (index > 0 ? "[" + (index+1) + "]" : "");

			result = tagName + pathIndex + result;
		}

		return result;
	}

	protected void initTarget(Element target, Event event) {
		this.relatedTargetXpath = getXPath(target);
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

	public void setCacheMap(CacheMap cacheMap) {
		this.cacheMap = cacheMap;
	}

	protected CacheMap getCacheMap() {
		if (this.cacheMap == null) {
			this.cacheMap = new CacheMap(30);
		}

		return this.cacheMap;
	}

	@Override
	public void prepareEvent() {
		if (el == null) {
			el = getCacheMap().resolveElement(getRelatedTargetXpath());
		}
	}
}
