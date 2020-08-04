val kotlin_version: String by extra
val serialization_version: String by extra
plugins {
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.70"
    `maven-publish`
}


group = "project"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

val implementation by configurations

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialization_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-common:$kotlin_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-annotations-common:$kotlin_version")


}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

//buildscript {
//    val kotlin_version: String by extra
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
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
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
