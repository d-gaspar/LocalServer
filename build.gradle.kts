import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"

    // desktop compose
    id("org.jetbrains.compose") version "0.3.0"
}

group = "me.daniel"
version = "1.0.0"

repositories {
    // desktop compose
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

    // ktor - netty
    maven("https://dl.bintray.com/kotlin/kotlinx")
    maven("https://dl.bintray.com/kotlin/ktor")
    mavenCentral()
}

dependencies {
    // desktop compose
    implementation(compose.desktop.currentOs)

    // ktor - netty
    implementation("io.ktor:ktor-server-core:1.5.2")
    implementation("io.ktor:ktor-server-netty:1.5.2")

    // jackson - json
    implementation("io.ktor:ktor-jackson:1.5.2")

    //https://stackabuse.com/reading-and-writing-json-in-kotlin-with-jackson/
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")

    implementation(kotlin("stdlib"))
    //implementation("ch.qos.logback:logback-classic:1.2.3")

    //implementation("io.ktor:ktor-html-builder:1.4.0")
    //implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

// desktop compose
compose.desktop {
    application {
        //disableDefaultConfiguration()
        //fromFiles(project.fileTree("libs/") { include("**/*.jar") })
        //mainJar.set(project.file("main.jar"))
        //dependsOn("mainJarTask")

        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg, //TargetFormat.Pkg //Mac
                TargetFormat.Exe, //TargetFormat.Msi //Windows
                TargetFormat.Deb  //TargetFormat.Rpm //Linux
            )

            outputBaseDir.set(project.buildDir.resolve("out"))

        }
    }
}