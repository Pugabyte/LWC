/*
 * Copyright (c) 2011-2013 Tyler Blair
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package org.getlwc.entity;

import org.getlwc.ItemStack;
import org.getlwc.command.CommandSender;
import org.getlwc.event.PlayerEventHandler;
import org.getlwc.lang.Locale;

import java.util.HashMap;
import java.util.Map;

import static org.getlwc.I18n._;

public abstract class Player extends PlayerEventHandler implements Entity, CommandSender {

    /**
     * This player's locale
     */
    private Locale locale = null;

    /**
     * Simple unpersisted attribute store for the player
     */
    private final Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * Get the item in the player's hand
     *
     * @return
     */
    public abstract ItemStack getItemInHand();

    /**
     * {@inheritDoc}
     */
    public void sendTranslatedMessage(String message, Object... arguments) {
        sendMessage(_(message, this, arguments));
    }

    /**
     * Get the locale for the player
     *
     * @return the player's locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the locale for the player
     *
     * @param locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Set an attribute in the store
     *
     * @param name
     * @param value
     */
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Get an attribute from the store
     *
     * @param name
     * @return
     */
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * Check if the player has the given attribute
     *
     * @param name
     * @return
     */
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    /**
     * Remove an attribute from the store
     *
     * @param name
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

}