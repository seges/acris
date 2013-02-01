package com.google.gwt.user.client.ui.impl;

import sk.seges.acris.recorder.client.utils.DOMUtil;

import com.google.gwt.user.client.Element;

public class FocusIDImplStandard extends FocusImplStandard {

	@Override
	public Element createFocusable() {
		Element result = super.createFocusable();
	    DOMUtil.generateId(result);
		return result;
	}
}