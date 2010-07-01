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
package sk.seges.acris.recorder.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sk.seges.acris.recorder.rpc.event.CloseChannelEvent;
import sk.seges.acris.recorder.rpc.event.ConversationEvent;
import sk.seges.acris.recorder.rpc.event.NewChannelEvent;
import sk.seges.acris.recorder.rpc.event.NewMessageEvent;
import sk.seges.acris.recorder.rpc.event.UserJoinEvent;
import sk.seges.acris.recorder.rpc.event.UserLeaveEvent;
import sk.seges.acris.recorder.rpc.event.filter.ChannelEventFilter;
import sk.seges.acris.recorder.rpc.service.Channel;
import sk.seges.acris.recorder.rpc.service.ConversationService;
import sk.seges.acris.recorder.server.channel.ChannelManager;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.logger.ServerLogger;
import de.novanic.eventservice.logger.ServerLoggerFactory;

/**
 * @author sstrohschein
 *         <br>Date: 16.09.2008
 *         <br>Time: 00:23:56
 */
@Service
public class ConversationServiceImpl extends RemoteEventServiceServlet implements ConversationService {
	
	private static final long serialVersionUID = -4235890591015799694L;

	private static final ServerLogger LOG = ServerLoggerFactory.getServerLogger(ConversationServiceImpl.class.getName());

    private static final Domain CONVERSATION_DOMAIN;
    private static final String GLOBAL_CHANNEL = "GlobalChannel";
    private static final ChannelManager myChannelManager;

    static {
        CONVERSATION_DOMAIN = DomainFactory.getDomain(ConversationEvent.CONVERSATION_DOMAIN);

        myChannelManager = new ChannelManager();
        myChannelManager.add(GLOBAL_CHANNEL);
    }

    public Channel createChannel(String aContact, String aChannelName) {
        Channel theChannel = myChannelManager.getChannelByName(aChannelName);
        if(theChannel == null) {
            theChannel = myChannelManager.add(aChannelName);
            addEvent(CONVERSATION_DOMAIN, new NewChannelEvent(aContact, theChannel));
            LOG.debug(aContact + " created channel and joined");
            joinInternal(aChannelName, aContact);
        }

        return theChannel;
    }

    public void closeChannel(String aContact, String aChannelName) {
        if(!GLOBAL_CHANNEL.equals(aChannelName)) {
            Channel theChannel = myChannelManager.getChannelByName(aChannelName);
            if(theChannel != null) {
                myChannelManager.remove(theChannel.getName());
                addEvent(CONVERSATION_DOMAIN, new CloseChannelEvent(aContact, theChannel));
            }
        }
    }

    public Channel join(String aContact, String aChannelName) {
        LOG.debug(aContact + " joined");
        joinInternal(aChannelName, aContact);
        return myChannelManager.getChannelByName(aChannelName);
    }

    public void leave(String aContact) {
        leaveInternal(aContact);
    }

    public void sendMessage(String aContact, String aMessage) {
        LOG.debug("Server-Message - " + aContact + ": " + aMessage);

        Channel theChannel = myChannelManager.getChannel(aContact);
        addEvent(CONVERSATION_DOMAIN, new NewMessageEvent(aContact, theChannel, aMessage));
    }

    public List<Channel> getChannels() {
        return myChannelManager.getChannels();
    }

    private void joinInternal(String aChannelName, String aContact) {
        leaveInternal(aContact);

        Channel theChannel = myChannelManager.join(aChannelName, aContact);
        addEvent(CONVERSATION_DOMAIN, new UserJoinEvent(aContact, theChannel));
        setEventFilter(CONVERSATION_DOMAIN, new ChannelEventFilter(aChannelName));
    }

    private void leaveInternal(String aContact) {
        Channel theChannel = myChannelManager.getChannel(aContact);
        if(theChannel != null) {
            theChannel.removeContact(aContact);
            addEvent(CONVERSATION_DOMAIN, new UserLeaveEvent(aContact, theChannel));
            if(theChannel.getContacts().isEmpty()) {
                closeChannel(aContact, theChannel.getName());
            }
        }
    }
}