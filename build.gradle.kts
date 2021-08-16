plugins {
    java
    kotlin("jvm") version "1.5.30-M1"
    id("com.github.johnrengelman.shadow") version ("7.0.0")
}

group = "com.wizn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}

tasks.shadowJar {
    manifest {
        attributes(mapOf("Main-Class" to "server.MainKt"))
    }
}