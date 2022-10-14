package com.jpmorgan.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HelloWorldController {

    private static final String HELLO_TEMPLATE = "Hello %s!!!";

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello-world")
    @ResponseBody
    public Greeting sayHello(@RequestParam(name="name", required = false, defaultValue = "nerdy nerd") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(HELLO_TEMPLATE, name));
    }

}