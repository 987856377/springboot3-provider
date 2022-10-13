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

package com.springboot.provider.common.spi.complex.agent.core.entity;

import java.util.LinkedList;

/**
 * Interceptor.
 */
public final class Interceptor {

    private String target;

    private String instanceAdvice;

    private String staticAdvice;

    private String constructAdvice;

    private LinkedList<TargetPoint> points;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getInstanceAdvice() {
        return instanceAdvice;
    }

    public void setInstanceAdvice(String instanceAdvice) {
        this.instanceAdvice = instanceAdvice;
    }

    public String getStaticAdvice() {
        return staticAdvice;
    }

    public void setStaticAdvice(String staticAdvice) {
        this.staticAdvice = staticAdvice;
    }

    public String getConstructAdvice() {
        return constructAdvice;
    }

    public void setConstructAdvice(String constructAdvice) {
        this.constructAdvice = constructAdvice;
    }

    public LinkedList<TargetPoint> getPoints() {
        return points;
    }

    public void setPoints(LinkedList<TargetPoint> points) {
        this.points = points;
    }
}
