/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.springboot.provider.common.spi.complex.agent.core.plugin;

import com.springboot.provider.common.spi.complex.agent.api.config.PluginConfiguration;
import com.springboot.provider.common.spi.complex.agent.api.spi.boot.PluginBootService;
import com.springboot.provider.common.spi.complex.agent.core.spi.AgentTypedSPIRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Plugin boot service manager.
 */
public final class PluginBootServiceManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(PluginBootServiceManager.class);

    private PluginBootServiceManager() {
    }

    /**
     * Start all services.
     *
     * @param pluginConfigurationMap plugin configuration map
     */
    public static void startAllServices(final Map<String, PluginConfiguration> pluginConfigurationMap) {
        for (Entry<String, PluginConfiguration> entry : pluginConfigurationMap.entrySet()) {
            AgentTypedSPIRegistry.getRegisteredServiceOptional(PluginBootService.class, entry.getKey()).ifPresent(optional -> {
                try {
                    LOGGER.info("Start plugin: {}", optional.getType());
                    optional.start(entry.getValue());
                    // CHECKSTYLE:OFF
                } catch (final Throwable ex) {
                    // CHECKSTYLE:ON
                    LOGGER.error("Failed to start service", ex);
                }
            });
        }
    }

    /**
     * Close all services.
     */
    public static void closeAllServices() {
        AgentTypedSPIRegistry.getAllRegisteredService(PluginBootService.class).forEach(each -> {
            try {
                each.close();
                // CHECKSTYLE:OFF
            } catch (final Throwable ex) {
                // CHECKSTYLE:ON
                LOGGER.error("Failed to close service", ex);
            }
        });
    }
}
