plugins {
    id("java")
    kotlin("jvm")
}

group = "io.github.octcarp.sustech.cs209a.linkgame"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

kotlin {
    jvmToolchain(22)
}