import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.8.10"
    id("com.adarshr.test-logger") version "3.2.0"
    id("com.diffplug.spotless") version "6.21.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
}

repositories {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.amshove.kluent:kluent:1.73")
}

sourceSets {
    test {
        java {
            srcDirs.add(File("src/test"))
        }
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/detekt/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("failed")

        // log full stacktrace of failed test (assertion library descriptive error)
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "17"
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "17"
}

kotlin {
    jvmToolchain(17)
}

spotless {
    kotlin {
        target("test/com/igorwojda/**/*.kt")
        ktlint()

        indentWithSpaces()
        endWithNewline()
    }
}
