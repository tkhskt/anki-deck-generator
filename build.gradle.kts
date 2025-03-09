import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
}

group = "com.tkhskt.ankideckgenerator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)

    // Include the Test API
    testImplementation(compose.desktop.uiTestJUnit4)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

compose.desktop {
    application {
        mainClass = "com.tkhskt.ankideckgenerator.MainKt"

        nativeDistributions {
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AnkiDeckGenerator"
            packageVersion = "1.0.0"
        }
    }
}
