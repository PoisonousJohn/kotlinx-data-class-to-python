import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

val kotlinVersion: String by extra
val mockitoCoreVersion: String by extra
val mockitoKotlinVersion: String by extra
val mockitoInlineVersion: String by extra
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

//    testCompile("junit", "junit", "4.12")
    testCompile("org.mockito:mockito-core:$mockitoCoreVersion")
    testCompile("org.mockito:mockito-inline:$mockitoInlineVersion")
    testCompile("com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVersion")
    testCompile("pl.pragmatists:JUnitParams:1.1.1")
    testCompile("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")


    kapt("com.google.auto.service:auto-service:1.0-rc7")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}