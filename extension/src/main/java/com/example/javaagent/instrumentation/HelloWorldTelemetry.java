//package com.example.javaagent.instrumentation;
//
//import io.opentelemetry.api.OpenTelemetry;
//
//public final class HelloWorldTelemetry {
//
//    private final OpenTelemetry openTelemetry;
//
//    public static HelloWorldTelemetry create(OpenTelemetry openTelemetry) {
//        return new HelloWorldTelemetry(openTelemetry);
//    }
//
//
//    private HelloWorldTelemetry(OpenTelemetry openTelemetry) {
//        this.openTelemetry = openTelemetry;
//    }
//
//    /** Start collecting metrics for given connection pool. */
//    public void registerMetrics(BasicDataSourceMXBean dataSource, String dataSourceName) {
//        GreetingMetrics.registerMetrics(openTelemetry, dataSource, dataSourceName);
//    }
//
//    /** Stop collecting metrics for given connection pool. */
//    public void unregisterMetrics(BasicDataSourceMXBean dataSource) {
//        GreetingMetrics.unregisterMetrics(dataSource);
//    }
//}
