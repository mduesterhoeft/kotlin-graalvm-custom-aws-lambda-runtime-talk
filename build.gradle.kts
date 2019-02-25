import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  kotlin("jvm") version "1.3.21"
  id("com.github.johnrengelman.shadow") version "4.0.3"
}

group = "com.github.md"
version = "1.0"

repositories {
  mavenCentral()
}

tasks.withType<ShadowJar> {
  archiveBaseName.set("package")
  manifest {
    attributes(mapOf("Main-Class" to "com.github.md.ApplicationKt"))
  }
}

dependencies {

  compile(kotlin("stdlib-jdk8"))
  compile(kotlin("reflect"))

  compile("org.slf4j:slf4j-log4j12:1.7.9")
  compile("commons-logging:commons-logging:1.2")

  compile("org.http4k:http4k-core:3.103.2")
  compile("com.amazonaws:aws-lambda-java-core:1.2.0")
  compile("com.fasterxml.jackson.core:jackson-databind:2.9.6")
  compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.6")
  compile("software.amazon.awssdk:dynamodb:2.2.0")
  compileOnly("com.oracle.substratevm:svm:1.0.0-rc12")

  testCompile("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}