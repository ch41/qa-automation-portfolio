plugins {
    java
    id("io.qameta.allure") version "4.3.0"
}

group = "com.qaautomation"
version = "1.0.0"

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(platform(libs.selenium.bom))

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.pioneer)
    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(libs.restassured)
    testImplementation(libs.restassured.json.schema)
    testImplementation(libs.selenide)
    testImplementation(libs.appium)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.assertj)
    testImplementation(libs.wiremock)

    testImplementation(libs.allure.junit5)
    testImplementation(libs.allure.selenide)
    testImplementation(libs.allure.restassured)

    testImplementation(libs.log4j.api)
    testImplementation(libs.log4j.core)
    testImplementation(libs.log4j.slf4j)
}

allure {
    version.set(libs.versions.allureCommandline.get())
}

val configPrefixes = listOf("env", "api.", "web.", "mobile.", "junit.")

tasks.withType<Test>().configureEach {
    systemProperties(
        System.getProperties().stringPropertyNames()
            .filter { name -> name == "env" || configPrefixes.any(name::startsWith) }
            .associateWith { System.getProperty(it) }
    )
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

fun registerTaggedTest(taskName: String, tag: String, description: String) {
    tasks.register<Test>(taskName) {
        group = "verification"
        this.description = description
        testClassesDirs = sourceSets.test.get().output.classesDirs
        classpath = sourceSets.test.get().runtimeClasspath
        useJUnitPlatform {
            includeTags(tag)
        }
    }
}

tasks.register<Test>("webSmokeTest") {
    group = "verification"
    description = "Runs the cross-browser web smoke suite"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform {
        includeTags("smoke")
        excludeTags("api", "mobile")
    }
}

registerTaggedTest("apiTest", "api", "Runs API tests")
registerTaggedTest("webTest", "web", "Runs web UI tests")
registerTaggedTest("mobileTest", "mobile", "Runs mobile tests (Appium server and device are required)")

tasks.test {
    description = "Runs API and web tests (mobile tests are excluded)"
    useJUnitPlatform {
        excludeTags("mobile")
    }
}

tasks.register("allTests") {
    group = "verification"
    description = "Runs API, web and mobile tests"
    dependsOn("apiTest", "webTest", "mobileTest")
}
