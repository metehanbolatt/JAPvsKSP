plugins {
    kotlin("jvm") version "2.1.20"
    id("org.jetbrains.kotlin.kapt") version "2.1.20"
}

group = "com.metehanbolat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":annotations"))
    kapt(project(":processor"))
}

kotlin {
    jvmToolchain(jdkVersion = 21)
}