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
package sk.seges.acris.recorder.client.control;

import java.util.List;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.recorder.client.listener.ConversationListenerAdapter;
import sk.seges.acris.recorder.client.ui.ChannelSelectListener;
import sk.seges.acris.recorder.client.ui.ConversationChannelCreatorDialog;
import sk.seges.acris.recorder.client.ui.ConversationChannelPanel;
import sk.seges.acris.recorder.client.ui.ConversationLoginPanel;
import sk.seges.acris.recorder.client.ui.ConversationMainPanel;
import sk.seges.acris.recorder.client.ui.ConversationMessagePanel;
import sk.seges.acris.recorder.client.ui.GWTConversationChannelCreatorDialog;
import sk.seges.acris.recorder.client.ui.message.MessageBox;
import sk.seges.acris.recorder.client.ui.message.MessageBoxCreator;
import sk.seges.acris.recorder.client.ui.message.MessageButtonListener;
import sk.seges.acris.recorder.rpc.event.ConversationEvent;
import sk.seges.acris.recorder.rpc.event.filter.ChannelEventFilter;
import sk.seges.acris.recorder.rpc.service.Channel;
import sk.seges.acris.recorder.rpc.service.ConversationService;
import sk.seges.acris.recorder.rpc.service.ConversationServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

/**
 * @author sstrohschein
 *         <br>Date: 16.09.2008
 *         <br>Time: 23:34:40
 */
public class ConversationControl
{
    private static final Domain CONVERSATION_DOMAIN = DomainFactory.getDomain(ConversationEvent.CONVERSATION_DOMAIN);
    private static final String GLOBAL_CHANNEL = "GlobalChannel";

    private ConversationMainPanel myConversationMainPanel;
    private ConversationServiceAsync myConversationService;
    private RemoteEventService myRemoteEventService;
    private Channel myChannel;
    private String myUser;

    public ConversationControl(ConversationMainPanel aConversationMainPanel) {
        myConversationMainPanel = aConversationMainPanel;

        myConversationService = (ConversationServiceAsync)mapService(GWT.create(ConversationService.class), "acris-service/conversationport");

        final RemoteEventServiceFactory theRemoteEventHandlerFactory = RemoteEventServiceFactory.getInstance();
        myRemoteEventService = theRemoteEventHandlerFactory.getRemoteEventService();

        requestChannelList(new DefaultAsyncCallback<List<Channel>>() {
            public void onSuccessCallback(List<Channel> aChannelList) {
                
                final ConversationLoginPanel theLoginPanel = myConversationMainPanel.getConversationLoginPanel();
                theLoginPanel.addLoginButtonListener(new ClickListener() {
                    public void onClick(Widget aSender) {
                        final boolean isLoginMode = theLoginPanel.isLogin();
                        boolean isActionSuccessful;
                        if(isLoginMode) {
                            //in case of login mode
                            isActionSuccessful = login();
                        } else {
                            //in case of logout mode
                            isActionSuccessful = logout();
                        }

                        if(isActionSuccessful) {
                            theLoginPanel.toggle(!isLoginMode);
                            myConversationMainPanel.getConversationMessagePanel().enable(isLoginMode);
                            myConversationMainPanel.getConversationChannelPanel().enable(isLoginMode);
                        }
                    }
                });
            }
        });
    }

    private void init() {
        myRemoteEventService.addListener(CONVERSATION_DOMAIN, new DefaultConversationListener(), new ChannelEventFilter(GLOBAL_CHANNEL), new DefaultAsyncCallback() {
            public void onSuccessCallback(Object aResult) {
                final ConversationChannelPanel theChannelPanel = myConversationMainPanel.getConversationChannelPanel();

                joinChannel(GLOBAL_CHANNEL, new DefaultAsyncCallback<Channel>() {
                    public void onSuccessCallback(Channel aResult) {
                        init(myConversationMainPanel.getConversationMessagePanel());
                        init(theChannelPanel);
                    }
                });
                requestChannelList(new DefaultAsyncCallback<List<Channel>>() {
                    public void onSuccessCallback(List<Channel> aChannelList) {
                        fillChannelList(theChannelPanel, aChannelList);
                    }
                });
            }
        });
    }

    private void init(final ConversationMessagePanel aConversationMessagePanel) {
        aConversationMessagePanel.addSendButtonListener(new ClickListener() {
            public void onClick(Widget aSender) {
                final String theMessage = aConversationMessagePanel.getMessageText();
                if(!theMessage.trim().equals("")) {
                    myConversationService.sendMessage(myUser, theMessage, new VoidAsyncCallback());
                    aConversationMessagePanel.resetMessageText();
                }
            }
        });
    }

    private void init(final ConversationChannelPanel aConversationChannelPanel) {
        aConversationChannelPanel.addAddChannelButtonListener(new ClickListener() {
            public void onClick(Widget aSender) {
                final ConversationChannelCreatorDialog theConversationChannelCreatorDialog = new GWTConversationChannelCreatorDialog();
                theConversationChannelCreatorDialog.addPopupListener(new PopupListener() {
                    public void onPopupClosed(PopupPanel aSender, boolean aIsAutoClosed) {
                        if(!theConversationChannelCreatorDialog.isCanceled()) {
                            //the new channel can be got with from the EventService and the user is joined by the server (ConversationService)
                            myConversationService.createChannel(myUser, theConversationChannelCreatorDialog.getChannelName(), new DefaultAsyncCallback<Channel>() {
                                public void onSuccessCallback(Channel aNewChannel) {
                                    switchChannel(aNewChannel);
                                }
                            });
                        }
                    }
                });
                theConversationChannelCreatorDialog.show();
            }
        });

        aConversationChannelPanel.addChannelSelectListener(new ChannelSelectListener() {
            public void onSelect(final String aChannel) {
                if(myChannel != null && !aChannel.equals(myChannel.getName())) {
                    final MessageBox theMessageBox = MessageBoxCreator.createYesNoMessage("Join channel \"" + aChannel + "\"?");
                    theMessageBox.addButtonListener(new MessageButtonListener() {
                        public void onClick(Button aButton) {
                            switch(aButton) {
                                case YES:
                                    myConversationService.join(myUser, aChannel, new VoidAsyncCallback<Channel>());
                                default:
                                    theMessageBox.close();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean login() {
        myUser = myConversationMainPanel.getConversationLoginPanel().getNicknameText();
        if(myUser == null || myUser.trim().equals("")) {
            MessageBoxCreator.createOkMessage("The username is empty. Please choose a username.");
            return false;
        } else if(myConversationMainPanel.getConversationChannelPanel().getContacts().contains(myUser)) {
            MessageBoxCreator.createOkMessage("The username does already exist. Please choose another username.");
            return false;
        } else {
            init();
            return true;
        }
    }

    private boolean logout() {
        myConversationService.leave(myUser, new VoidAsyncCallback());
        myRemoteEventService.removeListeners(CONVERSATION_DOMAIN);
        myConversationMainPanel.reset();
        return true;
    }

    private void joinChannel(String aChannelName, TrackingAsyncCallback<Channel> aCallback) {
        myConversationService.join(myUser, aChannelName, aCallback);
        myConversationMainPanel.getConversationChannelPanel().addContact(aChannelName, myUser);
    }

    private void requestChannelList(TrackingAsyncCallback<List<Channel>> aCallback) {
        myConversationService.getChannels(aCallback);
    }

    private void writeMessage(String aMessage) {
        writeMessage(null, aMessage);
    }

    private void writeMessage(String aUser, String aMessage) {
        String theMessage = aMessage;
        if(aUser != null) {
            theMessage = aUser + ": " + theMessage;
        }
        myConversationMainPanel.addMessageHistoryText(theMessage);
        myConversationMainPanel.getConversationMessagePanel().setFocus(true);
    }

    private void switchChannel(Channel aNewChannel) {
        myChannel = aNewChannel;
        myConversationMainPanel.clearMessageHistory();
    }

    private ServiceDefTarget mapService(Object aService, String aServiceMappingName) {
        String theServiceURL = GWT.getModuleBaseURL() + aServiceMappingName;
        ServiceDefTarget theServiceEndPoint = (ServiceDefTarget)aService;
        theServiceEndPoint.setServiceEntryPoint(theServiceURL);
        return theServiceEndPoint;
    }

    private void fillChannelList(ConversationChannelPanel aChannelPanel, List<Channel> aChannelList) {
        for(Channel theChannel: aChannelList) {
            final String theChannelName = theChannel.getName();
            aChannelPanel.addChannel(theChannelName);
            for(String theContact: theChannel.getContacts()) {
                aChannelPanel.addContact(theChannelName, theContact);
            }
        }
    }

    private abstract class DefaultAsyncCallback<T> extends TrackingAsyncCallback<T>
    {
        public void onFailureCallback(Throwable aThrowable) {
            GWT.log("Error on processing conversation!", aThrowable);
        }
    }

    private class VoidAsyncCallback<T> extends DefaultAsyncCallback<T>
    {
        public void onSuccessCallback(T aResult) {}
    }

    private class DefaultConversationListener extends ConversationListenerAdapter
    {
        public void newChannel(Channel aChannel) {
            myConversationMainPanel.getConversationChannelPanel().addChannel(aChannel.getName());
        }

        public void removedChannel(Channel aChannel) {
            myConversationMainPanel.getConversationChannelPanel().removeChannel(aChannel.getName());
        }

        public void userEntered(Channel aChannel, String aUser) {
            addUser(aChannel, aUser);
        }

        public void userLeaved(Channel aChannel, String aUser) {
            removeUser(aChannel, aUser);
        }

        public void newMessage(Channel aChannel, String aSender, String aMessage) {
            //to check the channel isn't required, because it is guaranteed by the ChannelEventFilter
            writeMessage(aSender, aMessage);
        }

        private void addUser(Channel aChannel, String aUser) {
            //switch the own channel
            if(myUser.equals(aUser)) {
                switchChannel(aChannel);
            }

            //add the contact to the channel bar
            final String theChannelName = aChannel.getName();
            myConversationMainPanel.getConversationChannelPanel().addContact(theChannelName, aUser);

            //write the entered message, if it is the current channel
            if(aChannel.equals(myChannel)) {
                writeMessage(aUser + " entered channel \"" + theChannelName + "\".");
            }
        }

        private void removeUser(Channel aChannel, String aUser) {
            //remove the contact from the channel bar
            myConversationMainPanel.getConversationChannelPanel().removeContact(aUser);

            //write the leaved message, if it is the current channel
            if(aChannel != null && aChannel.equals(myChannel)) {
                final String theChannelName = aChannel.getName();
                writeMessage(aUser + " leaved channel \"" + theChannelName + "\".");
            }
        }
    }
}