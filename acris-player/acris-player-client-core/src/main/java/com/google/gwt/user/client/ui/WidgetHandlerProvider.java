package com.google.gwt.user.client.ui;

import com.google.gwt.event.shared.HandlerManager;

public class WidgetHandlerProvider {
	public static HandlerManager getHandlerManager(Widget w) {
		return w.getHandlerManager();
	}
}
