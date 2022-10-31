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

package com.springboot.provider.common.spi.application.type.ordered.cache;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ordered services cached.
 */
public final class OrderedServicesCache {

    private OrderedServicesCache() {
    }

    private static volatile SoftReference<Map<Key, Map<?, ?>>> softCache = new SoftReference<>(new ConcurrentHashMap<>(128));
    
    /**
     * Find cached services.
     * 
     * @param spiClass SPI class
     * @param types types
     * @return cached services
     */
    public static Optional<Map<?, ?>> findCachedServices(final Class<?> spiClass, final Collection<?> types) {
        return Optional.ofNullable(softCache.get()).map(optional -> optional.get(new Key(spiClass, types)));
    }
    
    /**
     * Cache services.
     * 
     * @param spiClass SPI class
     * @param types types
     * @param services services to be cached
     */
    public static void cacheServices(final Class<?> spiClass, final Collection<?> types, final Map<?, ?> services) {
        Map<Key, Map<?, ?>> cache = softCache.get();
        if (null == cache) {
            synchronized (OrderedServicesCache.class) {
                cache = softCache.get();
                if (null == cache) {
                    cache = new ConcurrentHashMap<>(128);
                    softCache = new SoftReference<>(cache);
                }
            }
        }
        cache.put(new Key(spiClass, types), services);
    }

    private static final class Key {
        
        private final Class<?> clazz;
        
        private final Collection<?> types;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;

            Key key = (Key) o;

            if (!clazz.equals(key.clazz)) return false;
            return types.equals(key.types);
        }

        @Override
        public int hashCode() {
            int result = clazz.hashCode();
            result = 31 * result + types.hashCode();
            return result;
        }

        public Key(Class<?> clazz, Collection<?> types) {
            this.clazz = clazz;
            this.types = types;
        }
    }
}
