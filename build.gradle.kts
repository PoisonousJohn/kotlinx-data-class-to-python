val kotlinVersion: String by extra
val serializationVersion: String by extra
plugins {
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.70"
    kotlin("kapt") version "1.3.70"
    `maven-publish`
}

group = "com.anna.money"
version = "0.0.1"

repositories {
    mavenCentral()
}

val implementation by configurations

dependencies {
    implementation("com.google.auto.service:auto-service:1.0-rc7")
    implementation("com.google.auto.service:auto-service-annotations:1.0-rc7")
    api(project(":annotations"))
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    kapt(project(":codegen"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-common:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-annotations-common:$kotlinVersion")


}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
//            artifact(dokkaJar)
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/anna-money/kotlin-data-class-to-python")
//            credentials {
//                username =
//                    project.findProperty("gpr.user") as String? ?: System.getenv("GIT_USERNAME")
//                password =
//                    project.findProperty("gpr.password") as String? ?: System.getenv("GIT_PASSWORD")
//            }
        }
    }
}

//buildscript {
//    val kotlinVersion: String by extra
//    val shadow_version: String by extra
//
//    repositories {
//        maven("https://kotlin.bintray.com/kotlinx")
//        maven("https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
//        maven("https://dl.bintray.com/kotlin/kotlin-dev")
//        maven("https://dl.bintray.com/korlibs/korlibs")
//
//        google()
//        jcenter()
//    }
//    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
//        classpath("com.github.jengelman.gradle.plugins:shadow:$shadow_version")
//    }
//}
//
//allprojects {
//    repositories {
//        mavenLocal()
//        mavenCentral()
//        maven("https://dl.bintray.com/kotlin/kotlinx")
//        maven("https://dl.bintray.com/kotlin/ktor")
//        maven("https://dl.bintray.com/sargunster/maven")
//        maven("https://dl.bintray.com/kotlin/squash")
//        maven("https://dl.bintray.com/kotlin/kotlin-dev")
//
//        google()
//        jcenter()
//    }
//}
//
//tasks.register<Delete>("clean") {
//    delete(rootProject.buildDir)
//}
