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

package com.springboot.provider.common.spi.complex.agent.core.config.yaml;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * YAML plugin category configuration.
 */
public final class YamlPluginCategoryConfiguration {

    private Map<String, YamlPluginConfiguration> logging = new LinkedHashMap<>();

    private Map<String, YamlPluginConfiguration> metrics = new LinkedHashMap<>();

    private Map<String, YamlPluginConfiguration> tracing = new LinkedHashMap<>();

    public Map<String, YamlPluginConfiguration> getLogging() {
        return logging;
    }

    public Map<String, YamlPluginConfiguration> getMetrics() {
        return metrics;
    }

    public Map<String, YamlPluginConfiguration> getTracing() {
        return tracing;
    }
}
