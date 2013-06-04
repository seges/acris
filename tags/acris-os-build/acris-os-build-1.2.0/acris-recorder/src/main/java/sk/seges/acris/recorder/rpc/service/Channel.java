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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author sstrohschein
 *         <br>Date: 15.09.2008
 *         <br>Time: 23:59:58
 */
public class Channel implements IsSerializable
{
    private String myName;
    private List<String> myContacts;

    public Channel() {
        myContacts = new ArrayList<String>();
    }

    public Channel(String aName) {
        this();
        myName = aName;
    }

    public String getName() {
        return myName;
    }

    public void setName(String aName) {
        myName = aName;
    }

    public void addContact(String aContact) {
        myContacts.add(aContact);
    }

    public void removeContact(String aContact) {
        myContacts.remove(aContact);
    }

    public List<String> getContacts() {
        return myContacts;
    }

    public String toString() {
        return getName();
    }

    public boolean equals(Object anObject) {
        if(this == anObject) {
            return true;
        }
        if(anObject == null || getClass() != anObject.getClass()) {
            return false;
        }
        Channel theChannel = (Channel)anObject;
        return !(myName != null ? !myName.equals(theChannel.myName) : theChannel.myName != null);
    }

    public int hashCode() {
        return myName.hashCode();
    }
}