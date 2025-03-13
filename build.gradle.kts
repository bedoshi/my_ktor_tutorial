@file:Suppress("PropertyName")

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project

plugins {
    application
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "2.3.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.example.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Core
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    // Content Negotiation (JSON)
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // Auth (if needed)
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

    // Database (optional)
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Testing
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")

    // Kotlin テスト依存関係
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.1.10")
    // JUnit 5の依存関係をBOMを使って統一
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Athenz ZPE
    implementation("io.ktor:ktor-server-auth:2.0.0")
    implementation("io.ktor:ktor-server-auth-jwt:2.0.0")
    implementation("com.yahoo.athenz:athenz-zpe-lib:1.10.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
