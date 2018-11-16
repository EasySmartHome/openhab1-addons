/**
 * Copyright (c) 2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.cloudmatic.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;

public class CloudMatic {
    static Integer id;
    static String username;
    static String password;

    @ActionDoc(text = "Sends an email via CloudMatic")
    static public boolean sendCloudMaticMail(@ParamDoc(name = "to") String to,
            @ParamDoc(name = "subject") String subject, @ParamDoc(name = "message") String message) {

        if (!CloudMaticActionService.isProperlyConfigured) {
            return false;
        }
        return Mail.send(id.toString(), username, password, to, subject, message);
    }

    @ActionDoc(text = "Sends a SMS via CloudMatic")
    static public boolean sendCloudMaticSMS(@ParamDoc(name = "phoneNumber") String phoneNumber,
            @ParamDoc(name = "message") String message) {

        if (!CloudMaticActionService.isProperlyConfigured) {
            return false;
        }
        return SMS.send(id.toString(), username, password, phoneNumber, message);
    }
}
