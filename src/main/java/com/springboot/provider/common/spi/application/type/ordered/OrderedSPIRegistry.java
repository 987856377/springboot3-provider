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

package com.springboot.provider.common.spi.application.type.ordered;

import com.google.common.base.Preconditions;
import com.springboot.provider.common.spi.application.ApplicationServiceLoader;
import com.springboot.provider.common.spi.application.type.ordered.cache.OrderedServicesCache;

import java.util.*;

/**
 * Ordered SPI registry.
 */
public final class OrderedSPIRegistry {

    private OrderedSPIRegistry() {
    }

    /**
     * Get registered services by class type.
     *
     * @param spiClass class of ordered SPI
     * @param types types
     * @param <T> type of ordered SPI class
     * @return registered services
     */
    public static <T extends OrderedSPI<?>> Map<Class<?>, T> getRegisteredServicesByClass(final Class<T> spiClass, final Collection<Class<?>> types) {
        Collection<T> registeredServices = getRegisteredServices(spiClass);
        Map<Class<?>, T> result = new LinkedHashMap<>(registeredServices.size(), 1);
        for (T each : registeredServices) {
            types.stream().filter(type -> each.getTypeClass() == type).forEach(type -> result.put(type, each));
        }
        return result;
    }
    
    /**
     * Get registered services.
     *
     * @param spiClass class of ordered SPI
     * @param types types
     * @param <K> type of key
     * @param <V> type of ordered SPI class
     * @return registered services
     */
    public static <K, V extends OrderedSPI<?>> Map<K, V> getRegisteredServices(final Class<V> spiClass, final Collection<K> types) {
        return getRegisteredServices(spiClass, types, Comparator.naturalOrder());
    }
    
    /**
     * Get registered services.
     *
     * @param spiClass class of ordered SPI
     * @param types types
     * @param <K> type of key
     * @param <V> type of ordered SPI class
     * @param orderComparator order comparator
     * @return registered services
     */
    @SuppressWarnings("unchecked")
    public static <K, V extends OrderedSPI<?>> Map<K, V> getRegisteredServices(final Class<V> spiClass, final Collection<K> types, final Comparator<Integer> orderComparator) {
        Optional<Map<K, V>> cachedServices = OrderedServicesCache.findCachedServices(spiClass, types).map(optional -> (Map<K, V>) optional);
        if (cachedServices.isPresent()) {
            return cachedServices.get();
        }
        Collection<V> registeredServices = getRegisteredServices(spiClass, orderComparator);
        Map<K, V> result = new LinkedHashMap<>(registeredServices.size(), 1);
        for (V each : registeredServices) {
            types.stream().filter(type -> each.getTypeClass() == type.getClass()).forEach(type -> result.put(type, each));
        }
        OrderedServicesCache.cacheServices(spiClass, types, result);
        return result;
    }
    
    /**
     * Get registered services.
     *
     * @param spiClass class of ordered SPI
     * @param <T> type of ordered SPI class
     * @return registered services
     */
    public static <T extends OrderedSPI<?>> Collection<T> getRegisteredServices(final Class<T> spiClass) {
        return getRegisteredServices(spiClass, Comparator.naturalOrder());
    }
    
    private static <T extends OrderedSPI<?>> Collection<T> getRegisteredServices(final Class<T> spiClass, final Comparator<Integer> comparator) {
        Map<Integer, T> result = new TreeMap<>(comparator);
        for (T each : ApplicationServiceLoader.getServiceInstances(spiClass)) {
            Preconditions.checkArgument(!result.containsKey(each.getOrder()), "Found same order `%s` with `%s` and `%s`", each.getOrder(), result.get(each.getOrder()), each);
            result.put(each.getOrder(), each);
        }
        return result.values();
    }
}
