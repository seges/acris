package com.google.gwt.user.client.ui.impl;

import sk.seges.acris.recorder.client.utils.DOMUtil;

import com.google.gwt.user.client.Element;

public class FocusIDImplSafari extends FocusImplSafari {
	@Override
	public Element createFocusable() {
		Element result = super.createFocusable();
		DOMUtil.generateId(result);
		return result;
	}
}