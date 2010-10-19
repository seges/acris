package com.google.gwt.user.client.ui.impl;

import com.google.gwt.user.client.Element;

public class FocusIDImplSafari extends FocusImplSafari {
	  @Override
	  public native Element createFocusable() /*-{
	    // Use the infamous 'hidden input' trick to make a div effectively
	    // focusable.
	    var div = $doc.createElement('div');
	    @sk.seges.acris.client.utils.DOMUtil::generateId(Lcom/google/gwt/dom/client/Element;)(div);
	    var input = this.@com.google.gwt.user.client.ui.impl.FocusImplOld::createHiddenInput()();

	    // Add a mousedown listener to the div to focuses the input (to mimic the
	    // behavior of focusable elements on other browsers), and focus listeners
	    // on the input to propagate focus events back to the div.

	    // Note that we're using isolated lambda methods as the event listeners
	    // to avoid creating a memory leaks. (Lambdas here would create cycles
	    // involving the div and input).  This also allows us to share a single
	    // set of handlers among every focusable item.

	    input.addEventListener(
	      'blur',
	      this.@com.google.gwt.user.client.ui.impl.FocusImplOld::blurHandler,
	      false);

	    input.addEventListener(
	      'focus',
	      this.@com.google.gwt.user.client.ui.impl.FocusImplOld::focusHandler,
	      false);

	    div.addEventListener(
	      'mousedown',
	      this.@com.google.gwt.user.client.ui.impl.FocusImplOld::mouseHandler,
	      false);

	    div.appendChild(input);
	    return div;
	  }-*/;
}