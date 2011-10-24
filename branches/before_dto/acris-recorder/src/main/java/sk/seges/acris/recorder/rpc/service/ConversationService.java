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
package sk.seges.acris.recorder.rpc.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author sstrohschein
 *         <br>Date: 16.09.2008
 *         <br>Time: 00:03:48
 */
public interface ConversationService extends RemoteService
{
    Channel createChannel(String aContact, String aChannelName);

    void closeChannel(String aContact, String aChannelName);

    Channel join(String aContact, String aChannelName);

    void leave(String aContact);

    void sendMessage(String aContact, String aMessage);

    List<Channel> getChannels();
}