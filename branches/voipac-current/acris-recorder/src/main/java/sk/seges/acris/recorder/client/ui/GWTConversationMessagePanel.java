/*
 * GWTEventService
 * Copyright (c) 2008, GWTEventService Committers
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package sk.seges.acris.recorder.client.ui;

import com.google.gwt.user.client.ui.*;

import java.util.Collection;
import java.util.ArrayList;

/**
 * @author sstrohschein
 *         <br>Date: 16.09.2008
 *         <br>Time: 13:46:57
 */
public class GWTConversationMessagePanel extends HorizontalPanel implements ConversationMessagePanel
{
    private Button mySendButton;
    private TextBox myMessageTextBox;
    private Collection<ClickListener> mySendButtonClickListeners;

    public GWTConversationMessagePanel() {
        mySendButton = new Button();
        mySendButton.setText("Send");
        mySendButtonClickListeners = new ArrayList<ClickListener>();

        myMessageTextBox = new TextBox();
        myMessageTextBox.setMaxLength(250);
        myMessageTextBox.setWidth("240px");
        myMessageTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget aSender, char aKeyCode, int aModifiers) {
                if(aKeyCode == 13) {
                    mySendButton.click();
                }
            }
        });

        enable(false);

        //add the content
        setSpacing(5);
        setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        add(new Label("Message:"));
        add(myMessageTextBox);
        add(mySendButton);
    }

    public String getMessageText() {
        return myMessageTextBox.getText();
    }

    public void resetMessageText() {
        myMessageTextBox.setText("");
    }

    public void reset() {
        resetMessageText();
        for(ClickListener theClickListener: new ArrayList<ClickListener>(mySendButtonClickListeners)) {
            removeSendButtonListener(theClickListener);
        }
    }

    public void enable(boolean isEnable) {
        mySendButton.setEnabled(isEnable);
        myMessageTextBox.setEnabled(isEnable);
        if(isEnable) {
            myMessageTextBox.setFocus(true);
        }
    }

    public void setFocus(boolean isFocus) {
        myMessageTextBox.setFocus(isFocus);
    }

    public void addSendButtonListener(ClickListener aClickListener) {
        mySendButtonClickListeners.add(aClickListener);
        mySendButton.addClickListener(aClickListener);
    }

    public void removeSendButtonListener(ClickListener aClickListener) {
        mySendButton.removeClickListener(aClickListener);
        mySendButtonClickListeners.remove(aClickListener);
    }
}