/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.example.javaagent.instrumentation;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterBuilder;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import io.opentelemetry.instrumentation.api.internal.EmbeddedInstrumentationProperties;
import io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static net.bytebuddy.matcher.ElementMatchers.isConstructor;
import static net.bytebuddy.matcher.ElementMatchers.namedOneOf;

public class DemoHelloWorldInstrumentation implements TypeInstrumentation {

  private static String CLASS_NAME = "com.jpmorgan.sample.Greeting";

  private static OpenTelemetry telemetry = GlobalOpenTelemetry.get();


  private static Meter meter = telemetry.getMeterProvider().meterBuilder(CLASS_NAME).build();

  private static ObservableLongMeasurement click = meter.counterBuilder("click").setUnit("counter")
            .setDescription("Number of click")
            .buildObserver();;

  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    return AgentElementMatchers.hasSuperType(
        namedOneOf(CLASS_NAME));
  }

  @Override
  public void transform(TypeTransformer typeTransformer) {
    typeTransformer.applyAdviceToMethod(
            isConstructor(),
        this.getClass().getName() + "GreetingAdvice");
  }

  @SuppressWarnings("unused")
  public static class GreetingAdvice {

    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static void onEnter(@Advice.Argument(value = 0) long id, @Advice.Argument(value = 1) String name ) {
      click.record(1, Attributes.of(stringKey("name"), name).toBuilder().put(stringKey("id"), ""+id).build());
    }
  }
}
