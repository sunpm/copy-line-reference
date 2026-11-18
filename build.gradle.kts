plugins {
    java
    id("org.jetbrains.intellij.platform") version "2.20.1"
}

version = providers.gradleProperty("pluginVersion").getOrElse("0.1.0-dev")

repositories {
    mavenCentral()
    intellijPlatform { defaultRepositories() }
}

dependencies {
    intellijPlatform { webstorm("2025.1") }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

intellijPlatform {
    buildSearchableOptions = false
    // The settings panel uses plain Swing and has no GUI Designer forms to instrument.
    instrumentCode = false
    pluginConfiguration {
        ideaVersion {
            untilBuild = provider { null }
        }
    }
}
