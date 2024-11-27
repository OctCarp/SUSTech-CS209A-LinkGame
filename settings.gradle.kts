pluginManagement {
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

    plugins {
        kotlin("jvm") version "2.0.21" apply false
    }
}

rootProject.name = "linking-game"

include("game-client")
include("game-server")
include("game-common")

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