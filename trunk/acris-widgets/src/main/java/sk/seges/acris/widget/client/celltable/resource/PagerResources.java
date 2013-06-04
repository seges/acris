package sk.seges.acris.widget.client.celltable.resource;

import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.Style;


public interface PagerResources extends Resources {

	public interface PagerStyle extends Style {};

	@Source({ "pager.css" })
    Style simplePagerStyle();
}
