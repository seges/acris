/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.widgetideas.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.overrides.WidgetIterators;

import java.util.Iterator;

/**
 * {@link CollapsiblePanel} makes its contained contents able to collapse. By
 * default, the contents are fully expanded. When collapsed, the contents of the
 * panel will be displayed only when the user mouse hovers over the hover bar,
 * otherwise is will stay collapsed to the left. A change event is fired
 * whenever the {@link CollapsiblePanel} switched between its expanded and
 * collapsed states.
 * <p>
 * The default style name is gwt-CollapsiblePanel.
 * <p>
 * Planned enhancements: Allow panel to be collapsed in arbitrary direction.
 * 
 */
public class CollapsiblePanel extends Composite implements SourcesChangeEvents,
    HasWidgets, HasAnimation {
  /**
   * {@link CollapsiblePanel} styles.
   * 
   */
  public static class Styles {
    static String DEFAULT = "gwt-CollapsiblePanel";
    static String CONTAINER = "container";
    static String HOVER_BAR = "hover-bar";
  }

  /**
   * Current {@link CollapsiblePanel} state.
   */
  enum State {
    WILL_HIDE, HIDING, IS_HIDDEN, WILL_SHOW, SHOWING, IS_SHOWN, EXPANDED;

    public static void checkTo(State from, State to) {
      boolean valid = check(from, to);
      if (!valid) {
        throw new IllegalStateException(from + " -> " + to
            + " is an illegal state transition");
      }
    }

    private static boolean check(State from, State to) {

      if (to == EXPANDED || from == null) {
        return true;
      }
      switch (from) {
        case WILL_HIDE:
          return (to == IS_SHOWN || to == HIDING);
        case HIDING:
          return (to == IS_HIDDEN || to == SHOWING);
        case IS_HIDDEN:
          return (to == WILL_SHOW);
        case WILL_SHOW:
          return (to == SHOWING || to == IS_HIDDEN);
        case SHOWING:
          return (to == IS_SHOWN || to == HIDING);
        case IS_SHOWN:
          return to == WILL_HIDE;
        case EXPANDED:
          return to == HIDING;
        default:
          throw new IllegalStateException("Unknown state");
      }
    }

    public boolean shouldHide() {
      return this == SHOWING || this == IS_SHOWN;
    }

    public boolean shouldShow() {
      return this == HIDING || this == IS_HIDDEN;
    }
  }

  /**
   * Delays showing of the {@link CollapsiblePanel}.
   */
  private class DelayHide extends Timer {

    public void activate() {
      setState(State.WILL_HIDE);
      delayedHide.schedule(getDelayBeforeHide());
    }

    @Override
    public void run() {
      hide();
    }
  }

  /**
   * Delays showing of the {@link CollapsiblePanel}.
   */
  private class DelayShow extends Timer {

    public void activate() {
      setState(State.WILL_SHOW);
      delayedShow.schedule(getDelayBeforeShow());
    }

    @Override
    public void run() {
      show();
    }
  }

  private class HidingAnimation extends Animation {
    @Override
    public void onCancel() {
    }

    @Override
    public void onComplete() {
      setPanelPos(0);
      setState(State.IS_HIDDEN);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate(double progress) {
      int slide = (int) (startFrom - (progress * startFrom));
      setPanelPos(slide);
    }
  }

  private class ShowingAnimation extends Animation {

    @Override
    public void onCancel() {
      // Do nothing, must now be hiding.
    }

    @Override
    public void onComplete() {
      setPanelPos(maxOffshift);
      setState(State.IS_SHOWN);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate(double progress) {
      int pos = (int) ((double) (maxOffshift - startFrom) * progress);
      setPanelPos(pos + startFrom);
    }
  }

  private boolean animate = true;

  /**
   * Number of intervals used to display panel.
   */
  private int timeToSlide = 150;

  /**
   * How many milliseconds to delay a hover event before executing it.
   */
  private int delayBeforeShow = 300;

  /**
   * How many milliseconds to delay a hide event before executing it.
   */
  private int delayBeforeHide = 200;

  private State state;

  private ShowingAnimation overlayTimer = new ShowingAnimation();

  private HidingAnimation hidingTimer = new HidingAnimation();

  private DelayShow delayedShow = new DelayShow();

  private DelayHide delayedHide = new DelayHide();

  private int width;
  private int maxOffshift;
  private int currentOffshift;
  private int startFrom;
  private Panel container;
  private SimplePanel hoverBar;
  private ToggleButton collapseToggle;
  private AbsolutePanel master;
  private ChangeListenerCollection changeListeners = new ChangeListenerCollection();
  private Widget contents;

  /**
   * Constructor.
   */

  public CollapsiblePanel() {

    // Create the composite widget.
    master = new AbsolutePanel() {
      {
        sinkEvents(Event.ONMOUSEOUT | Event.ONMOUSEOVER);
      }

      @Override
      public void onBrowserEvent(Event event) {
        // Cannot handle browser events until contents are initialized.
        if (contents == null) {
          return;
        }
        if (!CollapsiblePanel.this.collapseToggle.isDown()) {
          switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEOUT:
              Element to = DOM.eventGetToElement(event);
              if (to == null && state == State.SHOWING) {
                // Linux hosted mode hack.
                return;
              }
              if (to != null && DOM.isOrHasChild(master.getElement(), to)) {
                break;
              }
              switch (state) {
                case WILL_SHOW:
                  setState(State.IS_HIDDEN);
                  delayedShow.cancel();
                  break;
                case SHOWING:
                  hide();
                  break;
                case IS_SHOWN:
                  delayedHide.activate();
                  break;
              }
              break;
            case Event.ONMOUSEOVER:
              if (state == State.WILL_HIDE) {
                setState(State.IS_SHOWN);
                delayedHide.cancel();
              }
              break;
          }
          super.onBrowserEvent(event);
        }
      }
    };

    DOM.setStyleAttribute(master.getElement(), "overflow", "visible");
    initWidget(master);
    setStyleName(Styles.DEFAULT);

    // Create hovering container.
    // Create the composite widget.
    hoverBar = new SimplePanel() {
      {
        sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEDOWN);
      }

      @Override
      public void onBrowserEvent(Event event) {
        // Cannot handle browser events until contents are initialized.
        if (contents == null) {
          return;
        }
        if (!CollapsiblePanel.this.collapseToggle.isDown()) {
          switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEOVER:
              switch (state) {
                case HIDING:
                  show();
                  break;
                case IS_HIDDEN:
                  delayedShow.activate();
                  break;
              }
              break;
            case Event.ONMOUSEDOWN:
              setCollapsedState(false, true);
          }
        }
      }
    };

    hoverBar.setStyleName(Styles.HOVER_BAR);
    master.add(hoverBar, 0, 0);

    // Create the contents container.
    container = new SimplePanel();
    container.setStyleName(Styles.CONTAINER);
    master.add(container, 0, 0);
    setState(State.EXPANDED);
  }

  /**
   * Constructor.
   */
  public CollapsiblePanel(Widget contents) {
    this();
    initContents(contents);
  }

  public void add(Widget w) {
    initContents(w);
  }

  public void addChangeListener(ChangeListener listener) {
    changeListeners.add(listener);
  }

  public void clear() {
    throw new IllegalStateException(
        "Collapsible Panel cannot be cleared once initialized");
  }

  public Widget getContents() {
    return contents;
  }

  /**
   * Gets the delay before hiding.
   * 
   * @return the delayBeforeHide
   */
  public int getDelayBeforeHide() {
    return delayBeforeHide;
  }

  /**
   * Gets the delay before showing the panel.
   * 
   * @return the delayBeforeShow
   */
  public int getDelayBeforeShow() {
    return delayBeforeShow;
  }

  /**
   * Gets the time to slide the panel out.
   * 
   * @return the timeToSlide
   */
  public int getTimeToSlide() {
    return timeToSlide;
  }

  /**
   * Hides the panel when the panel is the collapsible state. Does nothing if
   * the panel is expanded or not attached.
   */
  public void hide() {
    if (getState() != State.EXPANDED && isAttached()) {
      hiding();
    }
  }

  /**
   * Uses the given toggle button to control whether the panel is collapsed or
   * not.
   * 
   */
  public void hookupControlToggle(ToggleButton button) {
    this.collapseToggle = button;
    collapseToggle.setDown(!this.isCollapsed());
    collapseToggle.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        setCollapsedState(!CollapsiblePanel.this.collapseToggle.isDown(), true);
      }
    });
  }

  public boolean isAnimationEnabled() {
    return animate;
  }

  /**
   * Is the panel currently in its collapsed state.
   */
  public boolean isCollapsed() {
    return getState() != State.EXPANDED;
  }

  public Iterator<Widget> iterator() {
    return WidgetIterators.createWidgetIterator(this, new Widget[] {contents});
  }

  public boolean remove(Widget w) {
    return false;
  }

  public void removeChangeListener(ChangeListener listener) {
    changeListeners.remove(listener);
  }

  public void setAnimationEnabled(boolean enable) {
    animate = enable;
  }

  /**
   * Sets the state of the collapsible panel.
   * 
   * @param collapsed is the panel collapsed?
   */
  public void setCollapsedState(boolean collapsed) {
    setCollapsedState(collapsed, false);
  }

  /**
   * Sets the state of the collapsible panel.
   * 
   * @param collapsed is the panel collapsed?
   * @param fireEvents should the change listeners be fired?
   */
  public void setCollapsedState(boolean collapsed, boolean fireEvents) {
    if (isCollapsed() == collapsed) {
      return;
    }
    if (collapseToggle != null) {
      collapseToggle.setDown(!collapsed);
    }
    if (collapsed) {
      becomeCollapsed();
    } else {
      becomeExpanded();
    }
    if (fireEvents) {
      changeListeners.fireChange(this);
    }
  }

  /**
   * Sets the delay before the collapsible panel hides the panel after the user
   * leaves the panel.
   * 
   * @param delayBeforeHide the delayBeforeHide to set
   */
  public void setDelayBeforeHide(int delayBeforeHide) {
    this.delayBeforeHide = delayBeforeHide;
  }

  /**
   * Set delay before showing the panel after the user has hovered over the
   * hover bar.
   * 
   * @param delayBeforeShow the delayBeforeShow to set
   */
  public void setDelayBeforeShow(int delayBeforeShow) {
    this.delayBeforeShow = delayBeforeShow;
  }

  /**
   * Sets the contents of the hover bar.
   */
  public void setHoverBarContents(Widget bar) {
    hoverBar.setWidget(bar);
  }

  /**
   * Sets the time to slide the panel out.
   * 
   * @param timeToSlide the timeToSlide to set
   */
  public void setTimeToSlide(int timeToSlide) {
    this.timeToSlide = timeToSlide;
  }

  @Override
  public void setWidth(String width) {
    if (contents == null) {
      throw new IllegalStateException(
          "Cannot set the width of the collapsible panel before its contents are initialized");
    }
    contents.setWidth(width);
    refreshWidth();
  }

  /**
   * Shows the panel if the panel is in a collapsible state. Does nothing if the
   * panel is expanded or not attached.
   * 
   * Note: this method should only be called if the user's mouse will be over
   * the panel's contents after show() is executed.
   */
  public void show() {
    if (getState() != State.EXPANDED && isAttached()) {
      cancelAllTimers();
      setState(State.SHOWING);
      startFrom = currentOffshift;
      overlayTimer.run(this.getTimeToSlide());
    }
  }

  /**
   * Display this panel in its collapsed state. The panel's contents will be
   * hidden and only the hover var will be visible.
   */
  protected void becomeCollapsed() {
    cancelAllTimers();

    // Now hide.
    if (isAttached()) {
      adjustmentsForCollapsedState();
      hiding();
    } else {
      // onLoad will ensure this is correctly displayed. Here we just ensure
      // that the panel is the correct final hidden state. Forcing the state to
      // is hidden regardless of what is was.
      state = State.IS_HIDDEN;
    }
  }

  /**
   * Display this panel in its expanded state. The panel's contents will be
   * fully visible and take up all required space.
   */
  protected void becomeExpanded() {
    cancelAllTimers();
    // The master width needs to be readjusted back to it's original size.
    if (isAttached()) {
      adjustmentsForExpandedState();
    }
    setState(State.EXPANDED);
  }

  /**
   * This method is called immediately after a widget becomes attached to the
   * browser's document.
   */
  @Override
  protected void onLoad() {
    if (contents != null) {
      refreshWidth();
    }
  }

  protected void setPanelPos(int pos) {
    currentOffshift = pos;
    DOM.setStyleAttribute(container.getElement(), "left", pos - width + "px");
  }

  State getState() {
    return state;
  }

  private void adjustmentsForCollapsedState() {
    int hoverBarWidth = hoverBar.getOffsetWidth();
    int aboutHalf = (hoverBarWidth / 2) + 1;
    int newWidth = width + aboutHalf;
    maxOffshift = newWidth;

    // Width is now hoverBarWidth.
    master.setWidth(hoverBarWidth + "px");

    // clean up state.
    currentOffshift = width;
  }

  private void adjustmentsForExpandedState() {
    master.setWidth(width + "px");
    DOM.setStyleAttribute(container.getElement(), "left", "0px");
  }

  private void cancelAllTimers() {
    delayedHide.cancel();
    delayedShow.cancel();
    overlayTimer.cancel();
    hidingTimer.cancel();
  }

  private void hiding() {
    assert (isAttached());
    cancelAllTimers();
    setState(State.HIDING);
    startFrom = currentOffshift;
    hidingTimer.run(timeToSlide);
  }

  /**
   * Initialize the panel's contents.
   * 
   * @param contents contents
   */
  private void initContents(Widget contents) {
    if (this.contents != null) {
      throw new IllegalStateException("Contents have already be set");
    }

    this.contents = contents;
    container.add(contents);

    if (isAttached()) {
      refreshWidth();
    }
  }

  private void refreshWidth() {
    // Now include borders into master.
    width = container.getOffsetWidth();
    if (width == 0) {
      throw new IllegalStateException(
          "The underlying content width cannot be 0. Please ensure that the .container css style has a fixed width");
    }
    if (getState() == State.EXPANDED) {
      adjustmentsForExpandedState();
    } else {
      adjustmentsForCollapsedState();
      // we don't know if we just moved the mouse outside of the
      setPanelPos(0);
      state = State.IS_HIDDEN;
    }
  }

  private void setState(State state) {
    // checks are assuming animation.
    if (isAnimationEnabled()) {
      State.checkTo(this.state, state);
    }

    this.state = state;
  }
}
