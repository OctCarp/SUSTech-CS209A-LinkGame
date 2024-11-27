plugins {
    id("java")
    kotlin("jvm")
}

group = "io.github.octcarp.sustech.cs209a.linkgame"
version = "1.0-SNAPSHOT"

dependencies {
}

tasks.test {
    useJUnitPlatform()
}