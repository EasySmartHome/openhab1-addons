/**
 * Copyright (c) 2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.cloudmatic.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class CloudMaticActionService implements ActionService, ManagedService {
    static boolean isProperlyConfigured = false;

    public CloudMaticActionService() {
    }

    public void activate() {
    }

    public void deactivate() {
        // deallocate Resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    @Override
    public String getActionClassName() {
        return CloudMatic.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return CloudMatic.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void updated(Dictionary config) throws ConfigurationException {
        if (config != null) {
            String cmIdString = (String) config.get("id");
            if (cmIdString != null) {
                CloudMatic.id = Integer.valueOf(cmIdString);
            }
            CloudMatic.username = (String) config.get("username");
            CloudMatic.password = (String) config.get("password");

            if (StringUtils.isBlank(CloudMatic.username) || StringUtils.isBlank(CloudMatic.password)
                    || CloudMatic.id == null) {
                isProperlyConfigured = false;
                throw new ConfigurationException("cloudmatic",
                        "Parameters cloudmatic:id and cloudmatic:username and cloudmatic:password are mandatory and must be configured. Please check your cloudmatic.cfg!");
            }
            isProperlyConfigured = true;
        }
    }

}
