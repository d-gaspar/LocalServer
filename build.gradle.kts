import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"

    // desktop compose
    id("org.jetbrains.compose") version "1.0.0-alpha4-build328" // 2021-08-28
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
    //implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.+")

    // https://stackabuse.com/reading-and-writing-json-in-kotlin-with-jackson/
    //implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")
    //implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.5.21")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    //kotlinOptions.jvmTarget = "16"
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

            /*packageName = "LocalServer"
            packageVersion = "1.0.0"

            macOS {
                iconFile.set(project.file("src/main/resources/desktop_icon.icns"))
            }
            windows {
                iconFile.set(project.file("src/main/resources/desktop_icon.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/resources/desktop_icon.png"))
            }*/

            outputBaseDir.set(project.buildDir.resolve("out"))

        }
    }
}