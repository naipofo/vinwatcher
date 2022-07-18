import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("com.squareup.sqldelight") version "1.5.3"
    id("com.github.gmazzo.buildconfig") version "3.1.0"
    application
}

group = "com.naipofo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jsoup:jsoup:1.15.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")

    val ktorVersion = "2.0.2"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    implementation("org.kodein.di:kodein-di:7.12.0")

    implementation("dev.inmo:tgbotapi:2.2.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

sqldelight {
    database("StuffStore") {
        packageName = "com.naipofo"
    }
}

buildConfig {
    project.rootProject.file("tgapi.key").readText().let {
        buildConfigField("String", "TG_BOT_API_KEY", "\"${it}\"")
    }
}