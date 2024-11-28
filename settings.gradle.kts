pluginManagement {
    plugins {
        // Apply the foojay-resolver plugin to allow automatic download of JDKs
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"

        kotlin("jvm") version "2.1.0" apply false
    }

    repositories {
        // Aliyun Maven Repository
        maven("https://maven.aliyun.com/repository/public")
        // Aliyun Gradle Plugin Repository
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        // Maven Central repository
        mavenCentral()
        // Gradle plugins repository
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "linking-game"

include(
    "game-client",
    "game-server",
    "game-common"
)

dependencyResolutionManagement {
    repositories {
        // Aliyun Maven Repository
        maven("https://maven.aliyun.com/repository/public")
        // Aliyun Gradle Plugin Repository
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        // Maven Central repository
        mavenCentral()
        // Gradle plugins repository
        maven("https://plugins.gradle.org/m2/")
    }
}