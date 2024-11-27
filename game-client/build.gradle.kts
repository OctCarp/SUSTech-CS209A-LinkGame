plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.0.1"
    kotlin("jvm")
}

group = "io.github.octcarp.sustech.cs209a.linkgame"
version = "1.0-SNAPSHOT"


val junitVersion = "5.10.3"
val javaFxVersion = "23.0.1"

dependencies {
    implementation(project(":game-common"))

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation(kotlin("stdlib-jdk8"))
}

javafx {
    version = javaFxVersion
    modules("javafx.controls", "javafx.fxml")
}

jlink {
    options.set(listOf("--no-header-files", "--strip-debug", "--no-man-pages"))
    launcher {
        name = "linkgame"
    }
    imageName.set("linkgame")
}

application {
    mainClass.set("io.github.octcarp.sustech.cs209a.linkgame.client.GameMain")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(22)
}