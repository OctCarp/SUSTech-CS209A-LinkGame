plugins {
    id("java")
    kotlin("jvm")
}

group = "io.github.octcarp.sustech.cs209a.linkgame"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":game-common"))

    implementation("org.apache.commons:commons-csv:1.12.0")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
}
kotlin {
    jvmToolchain(22)
}