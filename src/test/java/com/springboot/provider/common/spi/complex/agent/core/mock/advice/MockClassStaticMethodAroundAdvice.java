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

package com.springboot.provider.common.spi.complex.agent.core.mock.advice;

import com.springboot.provider.common.spi.complex.agent.api.api.advice.ClassStaticMethodAroundAdvice;
import com.springboot.provider.common.spi.complex.agent.api.api.result.MethodInvocationResult;

import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unchecked")
public final class MockClassStaticMethodAroundAdvice implements ClassStaticMethodAroundAdvice {
    
    private final boolean rebase;

    public MockClassStaticMethodAroundAdvice(boolean rebase) {
        this.rebase = rebase;
    }
    
    public MockClassStaticMethodAroundAdvice() {
        this(false);
    }
    
    @Override
    public void beforeMethod(final Class<?> clazz, final Method method, final Object[] args, final MethodInvocationResult result) {
        List<String> queue = (List<String>) args[0];
        queue.add("before");
        if (rebase) {
            result.rebase("rebase static invocation method");
        }
    }
    
    @Override
    public void afterMethod(final Class<?> clazz, final Method method, final Object[] args, final MethodInvocationResult result) {
        List<String> queue = (List<String>) args[0];
        queue.add("after");
    }
    
    @Override
    public void onThrowing(final Class<?> clazz, final Method method, final Object[] args, final Throwable throwable) {
        List<String> queue = (List<String>) args[0];
        queue.add("exception");
    }
    
}
