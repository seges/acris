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
package sk.seges.acris.recorder.client.listener;

import sk.seges.acris.recorder.rpc.service.Channel;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * @author sstrohschein
 *         <br>Date: 16.09.2008
 *         <br>Time: 13:17:55
 */
public interface ConversationListener extends RemoteEventListener
{
    void newChannel(Channel aChannel);

    void removedChannel(Channel aChannel);

    void userEntered(Channel aChannel, String aUser);

    void userLeaved(Channel aChannel, String aUser);

    void newMessage(Channel aChannel, String aSender, String aMessage);
}