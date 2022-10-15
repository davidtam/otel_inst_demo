/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.example.javaagent.instrumentation;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static net.bytebuddy.matcher.ElementMatchers.namedOneOf;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

public class DemoHelloWorldInstrumentation implements TypeInstrumentation {

  private static String CLASS_NAME = "com.jpmorgan.sample.HelloWorldController";

  private static OpenTelemetry telemetry = GlobalOpenTelemetry.get();

  private static Meter meter = telemetry.getMeterProvider().meterBuilder(CLASS_NAME).build();

  private static ObservableLongMeasurement click =
      meter
          .counterBuilder("click")
          .setUnit("counter")
          .setDescription("Number of click")
          .buildObserver();
  ;

  static {
    meter.batchCallback(
        () -> {
          click.record(1, Attributes.of(stringKey("none"), "none"));
        },
        click);
  }

  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    return AgentElementMatchers.hasSuperType(namedOneOf(CLASS_NAME));
  }

  @Override
  public void transform(TypeTransformer typeTransformer) {
    typeTransformer.applyAdviceToMethod(
        namedOneOf("sayHello"), this.getClass().getName() + "$GreetingAdvice");
  }

  @SuppressWarnings("unused")
  public static class GreetingAdvice {

    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static void onEnter(@Advice.Argument(value = 0) String name) {
      System.out.println("inst name = " + name);
      click.record(1, Attributes.of(stringKey("name"), name));
    }
  }
}
