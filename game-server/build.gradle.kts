plugins {
    id("java")
    kotlin("jvm")

    application
}

group = "io.github.octcarp.sustech.cs209a.linkgame"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":game-common"))

    implementation("org.apache.commons:commons-csv:1.12.0")
}

application {
    mainClass.set("io.github.octcarp.sustech.cs209a.linkgame.server.ServerMain")
}

kotlin {
    jvmToolchain(22)
}

tasks.test {
}