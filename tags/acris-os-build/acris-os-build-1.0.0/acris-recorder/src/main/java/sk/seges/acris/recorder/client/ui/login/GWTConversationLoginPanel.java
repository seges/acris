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
package sk.seges.acris.recorder.client.ui.login;

import sk.seges.acris.recorder.client.ui.ConversationLoginPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author sstrohschein
 *         <br>Date: 16.09.2008
 *         <br>Time: 13:46:47
 */
public class GWTConversationLoginPanel extends HorizontalPanel implements ConversationLoginPanel
{
    private TextBox myNicknameTextBox;
    private Button myLoginLogoutButton;
    private boolean myIsLogin;

    public GWTConversationLoginPanel() {
        myLoginLogoutButton = new Button();
        myNicknameTextBox = new TextBox();
        
        //toggle to login mode
        toggle(true);

        myNicknameTextBox.setMaxLength(20);
        myNicknameTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget aSender, char aKeyCode, int aModifiers) {
                if(aKeyCode == 13) {
                    myLoginLogoutButton.click();
                }
            }
        });
        myNicknameTextBox.setFocus(true);

        //add the content
        setSpacing(5);
        add(new Label("Choose a nickname:"));
        add(myNicknameTextBox);
        add(myLoginLogoutButton);
    }

    public String getNicknameText() {
        return myNicknameTextBox.getText();
    }

    public void toggle(boolean isLogin) {
        myIsLogin = isLogin;
        if(myIsLogin) {
            myLoginLogoutButton.setText("Login");
        } else {
            myLoginLogoutButton.setText("Logout");
        }
        myNicknameTextBox.setEnabled(myIsLogin);
    }

    public boolean isLogin() {
        return myIsLogin;
    }

    public void addLoginButtonListener(ClickListener aLoginButtonListener) {
        myLoginLogoutButton.addClickListener(aLoginButtonListener);
    }
}