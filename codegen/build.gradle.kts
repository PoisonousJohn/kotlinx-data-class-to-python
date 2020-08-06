import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

val serializationVersion: String by extra
plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")

    api(project(":annotations"))
    implementation("com.google.auto.service:auto-service:1.0-rc7")

    testCompile("junit", "junit", "4.12")

    kapt("com.google.auto.service:auto-service:1.0-rc7")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}