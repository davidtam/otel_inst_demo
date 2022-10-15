//package com.example.javaagent.instrumentation;
//
//import io.opentelemetry.api.OpenTelemetry;
//import io.opentelemetry.api.common.Attributes;
//import io.opentelemetry.api.metrics.*;
//
//import io.opentelemetry.api.OpenTelemetry;
//import io.opentelemetry.api.common.Attributes;
//import io.opentelemetry.api.metrics.BatchCallback;
//import io.opentelemetry.api.metrics.ObservableLongMeasurement;
//import io.opentelemetry.instrumentation.api.internal.EmbeddedInstrumentationProperties;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class GreetingMetrics {
//    private static final String INSTRUMENTATION_NAME = "com.jpmorgan.sample.Greeting";
//
//    public static BatchCallback batchCallback(
//            Meter meter,
//            Runnable callback,
//            ObservableMeasurement observableMeasurement,
//            ObservableMeasurement... additionalMeasurements) {
//        return meter.batchCallback(callback, observableMeasurement, additionalMeasurements);
//    }
//    public static void registerMetrics(OpenTelemetry openTelemetry) {
//
//        MeterBuilder meterBuilder = openTelemetry.getMeterProvider().meterBuilder(INSTRUMENTATION_NAME);
//        String version = EmbeddedInstrumentationProperties.findVersion(INSTRUMENTATION_NAME);
//        if (version != null) {
//            meterBuilder.setInstrumentationVersion(version);
//        }
//        Meter meter = meterBuilder.build();
//        ObservableLongMeasurement measure = meter.upDownCounterBuilder("greeting.click")
//                .setUnit("counter")
//                .setDescription("Number of click")
//                .buildObserver();
//
//
//        BatchCallback callback =
//                batchCallback(
//                        () -> {
//                            measure.record(dataSource.getNumActive(), usedConnectionsAttributes);
//                            connections.record(dataSource.getNumIdle(), idleConnectionsAttributes);
//                            minIdleConnections.record(dataSource.getMinIdle(), attributes);
//                            maxIdleConnections.record(dataSource.getMaxIdle(), attributes);
//                            maxConnections.record(dataSource.getMaxTotal(), attributes);
//                        },
//                        connections,
//                        minIdleConnections,
//                        maxIdleConnections,
//                        maxConnections);
//
//        dataSourceMetrics.put(dataSource, callback);
//    }
//
//    public static void unregisterMetrics(BasicDataSourceMXBean dataSource) {
//        BatchCallback callback = dataSourceMetrics.remove(dataSource);
//        if (callback != null) {
//            callback.close();
//        }
//    }
//
//    private DataSourceMetrics() {}
//}
