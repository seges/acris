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
package sk.seges.acris.recorder.client.ui.message;

import com.google.gwt.user.client.ui.*;

import java.util.Map;
import java.util.HashMap;

/**
 * @author sstrohschein
 *         <br>Date: 25.09.2008
 *         <br>Time: 22:16:23
 */
public class GWTMessageBox extends DialogBox implements MessageBox
{
    private Map<MessageButtonListener.Button, Button> myMessageButtons;

    public GWTMessageBox(String aMessage, MessageButtonListener.Button... aMessageButtons) {
        setText(aMessage);
        center();

        add(createContent(aMessageButtons));
    }

    private Panel createContent(MessageButtonListener.Button... aMessageButtons) {
        HorizontalPanel theButtonPanel = new HorizontalPanel();
        theButtonPanel.setSpacing(5);
        theButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        myMessageButtons = new HashMap<MessageButtonListener.Button, Button>(aMessageButtons.length);
        for(MessageButtonListener.Button theMessageButton: aMessageButtons) {
            final Button theButton = new Button(theMessageButton.getDescription());
            myMessageButtons.put(theMessageButton, theButton);
            theButtonPanel.add(theButton);
        }
        return theButtonPanel;
    }

    public void close() {
        hide(true);
    }

    public void addButtonListener(MessageButtonListener aButtonListener) {
        for(Map.Entry<MessageButtonListener.Button, Button> theButtonMapping: myMessageButtons.entrySet()) {
            final MessageButtonListener.Button theMessageButton = theButtonMapping.getKey();
            final Button theButton = theButtonMapping.getValue();
            theButton.addClickListener(new ButtonListener(aButtonListener, theMessageButton));
        }
    }

    private class ButtonListener implements ClickListener
    {
        private MessageButtonListener myMessageButtonListener;
        private MessageButtonListener.Button myButton;

        private ButtonListener(MessageButtonListener aMessageButtonListener, MessageButtonListener.Button aButton) {
            myMessageButtonListener = aMessageButtonListener;
            myButton = aButton;
        }

        public void onClick(Widget aSender) {
            myMessageButtonListener.onClick(myButton);
        }
    }
}