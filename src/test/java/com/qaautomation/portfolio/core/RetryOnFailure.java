package com.qaautomation.portfolio.core;

import org.junitpioneer.jupiter.RetryingTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RetryingTest(maxAttempts = 2)
public @interface RetryOnFailure {
}
