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

/**
 * @author sstrohschein
 *         <br>Date: 21.09.2008
 *         <br>Time: 18:14:54
 */
public class GWTConversationChannelCreatorDialog extends DialogBox implements ConversationChannelCreatorDialog
{
    private String myChannelName;

    public GWTConversationChannelCreatorDialog() {
        setText("New channel");
        setAnimationEnabled(true);

        setWidget(createContentPanel());

        center();
    }

    private Panel createContentPanel() {
        final Button theCreateChannelButton = new Button("Create");
        final Button theCancelButton = new Button("Cancel");

        final TextBox theChannelNameText = new TextBox();
        theChannelNameText.setMaxLength(30);
        theChannelNameText.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget aSender, char aKeyCode, int aModifiers) {
                switch(aKeyCode) {
                    case 13: theCreateChannelButton.click();
                             break;
                    case 27: theCancelButton.click();
                }
            }
        });
        theChannelNameText.setFocus(true);

        theCreateChannelButton.addClickListener(new ClickListener() {
            public void onClick(Widget aSender) {
                String theChannelName = theChannelNameText.getText();
                close(theChannelName);
            }
        });

        theCancelButton.addClickListener(new ClickListener() {
            public void onClick(Widget aSender) {
                close(null);
            }
        });

        HorizontalPanel theActionPanel = new HorizontalPanel();
        theActionPanel.setSpacing(5);
        theActionPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        theActionPanel.add(theCreateChannelButton);
        theActionPanel.add(theCancelButton);

        VerticalPanel theContentPanel = new VerticalPanel();
        theContentPanel.setSpacing(5);
        theContentPanel.add(theChannelNameText);
        theContentPanel.add(theActionPanel);

        return theContentPanel;
    }

    private void close(String aChannelName) {
        myChannelName = aChannelName;
        hide(true);
    }

    public boolean isCanceled() {
        return myChannelName == null || myChannelName.trim().isEmpty();
    }

    public String getChannelName() {
        return myChannelName;
    }
}