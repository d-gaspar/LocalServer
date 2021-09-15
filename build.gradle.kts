import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"

    // desktop compose
    //id("org.jetbrains.compose") version "0.3.0"
    id("org.jetbrains.compose") version "1.0.0-alpha4-build328" // 2021-08-28
    //id("org.jetbrains.compose") version (System.getenv("COMPOSE_TEMPLATE_COMPOSE_VERSION") ?: "1.0.0-alpha4-build328")
}

group = "me.daniel"
version = "1.0.0"

repositories {
    // desktop compose
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

    mavenCentral()
    google()
}

dependencies {
    // desktop compose
    implementation(compose.desktop.currentOs)

    // jackson - json
    implementation("io.ktor:ktor-jackson:1.6.3")

    // https://stackabuse.com/reading-and-writing-json-in-kotlin-with-jackson/
    //implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")
    //implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

// desktop compose
compose.desktop {
    application {

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