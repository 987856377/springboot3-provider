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

package com.springboot.provider.common.spi.complex.agent.core.config.loader;

import com.google.common.base.Preconditions;
import com.springboot.provider.common.spi.complex.agent.api.config.AgentConfiguration;
import com.springboot.provider.common.spi.complex.agent.core.config.path.AgentPathBuilder;
import com.springboot.provider.common.spi.complex.agent.core.config.yaml.YamlAgentConfiguration;
import com.springboot.provider.common.spi.complex.agent.core.config.yaml.engine.YamlEngine;
import com.springboot.provider.common.spi.complex.agent.core.config.yaml.swapper.YamlAgentConfigurationSwapper;

import java.io.File;
import java.io.IOException;

/**
 * Agent configuration loader.
 */
public final class AgentConfigurationLoader {

    private static final String DEFAULT_CONFIG_PATH = "/conf/agent.yaml";

    private AgentConfigurationLoader() {
    }

    /**
     * Load configuration of ShardingSphere agent.
     *
     * @return configuration of ShardingSphere agent
     * @throws IOException IO exception
     */
    public static AgentConfiguration load() throws IOException {
        File configFile = new File(AgentPathBuilder.getAgentPath(), DEFAULT_CONFIG_PATH);
        return YamlAgentConfigurationSwapper.swap(load(configFile));
    }

    private static YamlAgentConfiguration load(final File yamlFile) throws IOException {
        YamlAgentConfiguration result = YamlEngine.unmarshal(yamlFile, YamlAgentConfiguration.class);
        Preconditions.checkNotNull(result, "Agent configuration file `%s` is invalid", yamlFile.getName());
        return result;
    }
}
