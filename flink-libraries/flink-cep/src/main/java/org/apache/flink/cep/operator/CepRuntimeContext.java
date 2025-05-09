/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.cep.operator;

import org.apache.flink.annotation.Internal;
import org.apache.flink.api.common.JobInfo;
import org.apache.flink.api.common.TaskInfo;
import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.api.common.accumulators.DoubleCounter;
import org.apache.flink.api.common.accumulators.Histogram;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.api.common.cache.DistributedCache;
import org.apache.flink.api.common.externalresource.ExternalResourceInfo;
import org.apache.flink.api.common.functions.BroadcastVariableInitializer;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.AggregatingState;
import org.apache.flink.api.common.state.AggregatingStateDescriptor;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.metrics.groups.OperatorMetricGroup;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * A wrapper class for the {@link RuntimeContext}.
 *
 * <p>This context only exposes the functionality needed by the pattern process function and
 * iterative condition function. Consequently, state access, accumulators, broadcast variables and
 * the distributed cache are disabled.
 */
@Internal
class CepRuntimeContext implements RuntimeContext {

    private final RuntimeContext runtimeContext;

    CepRuntimeContext(final RuntimeContext runtimeContext) {
        this.runtimeContext = checkNotNull(runtimeContext);
    }

    @Override
    public OperatorMetricGroup getMetricGroup() {
        return runtimeContext.getMetricGroup();
    }

    @Override
    public <T> TypeSerializer<T> createSerializer(TypeInformation<T> typeInformation) {
        return runtimeContext.createSerializer(typeInformation);
    }

    @Override
    public Map<String, String> getGlobalJobParameters() {
        return runtimeContext.getGlobalJobParameters();
    }

    @Override
    public boolean isObjectReuseEnabled() {
        return runtimeContext.isObjectReuseEnabled();
    }

    @Override
    public ClassLoader getUserCodeClassLoader() {
        return runtimeContext.getUserCodeClassLoader();
    }

    @Override
    public void registerUserCodeClassLoaderReleaseHookIfAbsent(
            String releaseHookName, Runnable releaseHook) {
        runtimeContext.registerUserCodeClassLoaderReleaseHookIfAbsent(releaseHookName, releaseHook);
    }

    @Override
    public DistributedCache getDistributedCache() {
        return runtimeContext.getDistributedCache();
    }

    @Override
    public Set<ExternalResourceInfo> getExternalResourceInfos(String resourceName) {
        return runtimeContext.getExternalResourceInfos(resourceName);
    }

    @Override
    public JobInfo getJobInfo() {
        return runtimeContext.getJobInfo();
    }

    @Override
    public TaskInfo getTaskInfo() {
        return runtimeContext.getTaskInfo();
    }

    // -----------------------------------------------------------------------------------
    // Unsupported operations
    // -----------------------------------------------------------------------------------

    @Override
    public <V, A extends Serializable> void addAccumulator(
            final String name, final Accumulator<V, A> accumulator) {
        throw new UnsupportedOperationException("Accumulators are not supported.");
    }

    @Override
    public <V, A extends Serializable> Accumulator<V, A> getAccumulator(final String name) {
        throw new UnsupportedOperationException("Accumulators are not supported.");
    }

    @Override
    public IntCounter getIntCounter(final String name) {
        throw new UnsupportedOperationException("Int counters are not supported.");
    }

    @Override
    public LongCounter getLongCounter(final String name) {
        throw new UnsupportedOperationException("Long counters are not supported.");
    }

    @Override
    public DoubleCounter getDoubleCounter(final String name) {
        throw new UnsupportedOperationException("Double counters are not supported.");
    }

    @Override
    public Histogram getHistogram(final String name) {
        throw new UnsupportedOperationException("Histograms are not supported.");
    }

    @Override
    public boolean hasBroadcastVariable(final String name) {
        throw new UnsupportedOperationException("Broadcast variables are not supported.");
    }

    @Override
    public <RT> List<RT> getBroadcastVariable(final String name) {
        throw new UnsupportedOperationException("Broadcast variables are not supported.");
    }

    @Override
    public <T, C> C getBroadcastVariableWithInitializer(
            final String name, final BroadcastVariableInitializer<T, C> initializer) {
        throw new UnsupportedOperationException("Broadcast variables are not supported.");
    }

    @Override
    public <T> ValueState<T> getState(final ValueStateDescriptor<T> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <T> ListState<T> getListState(final ListStateDescriptor<T> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <T> ReducingState<T> getReducingState(final ReducingStateDescriptor<T> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <IN, ACC, OUT> AggregatingState<IN, OUT> getAggregatingState(
            final AggregatingStateDescriptor<IN, ACC, OUT> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <UK, UV> MapState<UK, UV> getMapState(final MapStateDescriptor<UK, UV> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <T> org.apache.flink.api.common.state.v2.ValueState<T> getState(
            org.apache.flink.api.common.state.v2.ValueStateDescriptor<T> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <T> org.apache.flink.api.common.state.v2.ListState<T> getListState(
            org.apache.flink.api.common.state.v2.ListStateDescriptor<T> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <T> org.apache.flink.api.common.state.v2.ReducingState<T> getReducingState(
            org.apache.flink.api.common.state.v2.ReducingStateDescriptor<T> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <IN, ACC, OUT>
            org.apache.flink.api.common.state.v2.AggregatingState<IN, OUT> getAggregatingState(
                    org.apache.flink.api.common.state.v2.AggregatingStateDescriptor<IN, ACC, OUT>
                            stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }

    @Override
    public <UK, UV> org.apache.flink.api.common.state.v2.MapState<UK, UV> getMapState(
            org.apache.flink.api.common.state.v2.MapStateDescriptor<UK, UV> stateProperties) {
        throw new UnsupportedOperationException("State is not supported.");
    }
}
