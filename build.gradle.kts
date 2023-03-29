plugins {
    id("org.springframework.boot") version "2.7.7"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"

    id("org.jetbrains.kotlin.jvm") version "1.8.0"
    id("org.jetbrains.kotlin.kapt") version "1.8.0"

    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.spring") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.8.0"
}

group = "jpabook"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileKotlin {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}