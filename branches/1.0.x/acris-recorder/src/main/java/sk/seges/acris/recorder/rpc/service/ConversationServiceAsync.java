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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConversationServiceAsync
{
    void createChannel(String aContact, String aChannelName, AsyncCallback<Channel> async);

    void closeChannel(String aContact, String aChannelName, AsyncCallback async);

    void join(String aContact, String aChannelName, AsyncCallback<Channel> async);

    void leave(String aContact, AsyncCallback async);

    void sendMessage(String aContact, String aMessage, AsyncCallback async);

    void getChannels(AsyncCallback<List<Channel>> async);
}
