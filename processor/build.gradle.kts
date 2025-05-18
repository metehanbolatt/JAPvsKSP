plugins {
    kotlin("jvm")
}

group = "com.metehanbolat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.palantir.javapoet:javapoet:0.7.0")
    implementation(project(":annotations"))
}

kotlin {
    jvmToolchain(21)
}