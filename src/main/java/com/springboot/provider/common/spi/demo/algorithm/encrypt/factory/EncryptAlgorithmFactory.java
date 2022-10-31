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

package com.springboot.provider.common.spi.demo.algorithm.encrypt.factory;

import com.springboot.provider.common.spi.demo.algorithm.AlgorithmConfiguration;
import com.springboot.provider.common.spi.demo.algorithm.encrypt.EncryptAlgorithm;
import com.springboot.provider.common.spi.demo.algorithm.ShardingSphereAlgorithmFactory;
import com.springboot.provider.common.spi.application.ApplicationServiceLoader;
import com.springboot.provider.common.spi.application.type.typed.TypedSPIRegistry;

/**
 * Encrypt algorithm factory.
 */
public final class EncryptAlgorithmFactory {
    
    static {
        ApplicationServiceLoader.register(EncryptAlgorithm.class);
    }

    private EncryptAlgorithmFactory() {
    }

    /**
     * Create new instance of encrypt algorithm.
     * 
     * @param encryptAlgorithmConfig encrypt algorithm configuration
     * @param <I> type of to be encrypted data
     * @param <O> type of to be decrypted data
     * @return created instance
     */
    public static <I, O> EncryptAlgorithm<I, O> newInstance(final AlgorithmConfiguration encryptAlgorithmConfig) {
        return ShardingSphereAlgorithmFactory.createAlgorithm(encryptAlgorithmConfig, EncryptAlgorithm.class);
    }
    
    /**
     * Judge whether contains encrypt algorithm.
     * 
     * @param encryptAlgorithmType encrypt algorithm type
     * @return contains encrypt algorithm or not
     */
    public static boolean contains(final String encryptAlgorithmType) {
        return TypedSPIRegistry.findRegisteredService(EncryptAlgorithm.class, encryptAlgorithmType).isPresent();
    }
}
