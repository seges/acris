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

import com.google.gwt.user.client.ui.ClickListener;

import java.util.Set;

/**
 * @author sstrohschein
 *         <br>Date: 16.09.2008
 *         <br>Time: 23:02:42
 */
public interface ConversationChannelPanel
{
    boolean addChannel(String aChannel);

    boolean removeChannel(String aChannel);

    boolean addContact(String aChannel, String aContactName);

    boolean removeContact(String aContactName);

    Set<String> getContacts();

    void reset();

    void enable(boolean isEnable);

    void addAddChannelButtonListener(ClickListener aClickListener);

    void removeAddChannelButtonListener(ClickListener aClickListener);

    void addChannelSelectListener(ChannelSelectListener aChannelSelectListener);
}