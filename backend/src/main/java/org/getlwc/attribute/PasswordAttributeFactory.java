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

package org.getlwc.attribute;

import org.getlwc.AccessProvider;
import org.getlwc.Engine;
import org.getlwc.attribute.HashedString;
import org.getlwc.attribute.HashedStringAttribute;
import org.getlwc.attribute.ProtectionAttributeFactory;
import org.getlwc.command.Command;
import org.getlwc.command.CommandContext;
import org.getlwc.command.CommandException;
import org.getlwc.command.SenderType;
import org.getlwc.entity.Player;
import org.getlwc.model.AbstractAttribute;
import org.getlwc.model.Protection;
import org.getlwc.role.Role;
import org.getlwc.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class PasswordAttributeFactory implements ProtectionAttributeFactory<HashedString> {

    /**
     * Attribute name
     */
    public static final String NAME = "password";

    /**
     * The engine instance
     */
    private Engine engine;

    public PasswordAttributeFactory(Engine engine) {
        this.engine = engine;

        try {
            engine.getCommandHandler().registerCommands(this);
        } catch (CommandException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return NAME;
    }

    public AbstractAttribute<HashedString> createAttribute() {
        return new PasswordAttribute();
    }

    @Command(
            command = "lwc password",
            description = "Unlocks a protection via password if you have attempted to open one",
            permission = "lwc.password",
            aliases = {"cunlock"},
            accepts = SenderType.PLAYER,
            min = 1
    )
    public void passwordCommand(CommandContext context) {
        Player player = (Player) context.getCommandSender();
        String password = StringUtils.join(context.getArgumentsArray());

        Object request = player.getAttribute("password_request");

        if (request == null) {
            player.sendTranslatedMessage("&4You have no pending /cunlock.");
            return;
        }

        PasswordAttribute attribute = (PasswordAttribute) request;

        if (attribute.getValue().matches(password)) {
            int protectionId = (Integer) player.getAttribute("password_protection_id");
            player.sendTranslatedMessage("&2Granted access to the protection!");
            player.removeAttribute("password_request");
            player.removeAttribute("password_protection_id");

            if (!player.hasAttribute("password_authorized")) {
                player.setAttribute("password_authorized", new HashSet<Integer>());
            }

            Set<Integer> authorized = (Set<Integer>) player.getAttribute("password_authorized");
            authorized.add(protectionId);
        } else {
            player.sendTranslatedMessage("&4Invalid password.");
        }
    }

    private class PasswordAttribute extends HashedStringAttribute implements AccessProvider {

        public PasswordAttribute() {
            super(engine, NAME, "");
        }

        public Role.Access getAccess(Protection protection, Player player) {
            if (player.hasAttribute("password_authorized")) {
                Set<Integer> authorized = (Set<Integer>) player.getAttribute("password_authorized");

                if (authorized.contains(protection.getId())) {
                    return Role.Access.MEMBER;
                }
            }

            player.sendTranslatedMessage("&4This protection is locked by a password.\n&4To enter the password, use: &3/cunlock <password>");
            player.setAttribute("password_request", this);
            player.setAttribute("password_protection_id", protection.getId());
            return Role.Access.EXPLICIT_DENY;
        }

    }

}