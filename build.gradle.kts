import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.20"
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "my.id.kagchi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.toedter:jcalendar:1.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

application {
    mainClass.set("my.id.kagchi.Main")
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "my.id.kagchi.Main"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}