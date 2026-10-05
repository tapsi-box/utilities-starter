import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
  kotlin("jvm") version "2.4.20"
  kotlin("plugin.spring") version "2.4.20"
  id("io.spring.dependency-management") version "1.1.7"

  id("com.vanniktech.maven.publish") version "0.37.0"
  id("com.diffplug.spotless") version "8.10.3"
  id("io.gitlab.arturbosch.detekt") version "1.23.8"
  `java-library`
}

group = "box.tapsi.libs"
version = "1.0.0"
description = "utilities-starter"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework:spring-context:7.0.9")
  implementation("org.springframework.boot:spring-boot-autoconfigure:4.1.1")
  implementation("io.projectreactor:reactor-core:3.8.7")
  implementation("org.springframework.security:spring-security-crypto:7.1.1")
  implementation("io.jsonwebtoken:jjwt-api:0.13.0")
  implementation("tools.jackson.core:jackson-databind:3.1.5")
  implementation("tools.jackson.module:jackson-module-kotlin:3.1.5")
  implementation("io.micrometer:micrometer-core:1.17.1")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.3.2")
  implementation("io.projectreactor.addons:reactor-extra:3.6.1")
  implementation("org.slf4j:slf4j-api:2.0.18")
  implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
  implementation("io.grpc:grpc-api:1.83.1")

  api("com.appmattus.fixture:fixture:1.2.0")

  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")

  testImplementation("org.springframework.boot:spring-boot-starter-test:4.1.1")
  testImplementation("org.springframework.boot:spring-boot-jackson:4.1.1")
  testImplementation(kotlin("test"))
  testImplementation("io.projectreactor:reactor-test:3.8.7")
  testImplementation("org.mockito.kotlin:mockito-kotlin:6.4.0")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
  // Keep the published metadata readable by Kotlin 2.2+ (the Spring Boot 4 baseline),
  // while the library is compiled with a newer Kotlin compiler.
  coreLibrariesVersion = "2.2.21"
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
    languageVersion.set(KotlinVersion.KOTLIN_2_2)
    apiVersion.set(KotlinVersion.KOTLIN_2_2)
    freeCompilerArgs.addAll(
      "-Xjsr305=strict",
      "-Xannotation-default-target=param-property",
    )
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

mavenPublishing {
  publishToMavenCentral()
  signAllPublications()

  pom {
    name.set("utilities-starter")
    description.set("Tapsi common utilities starter for Spring Boot applications")
    url.set("https://github.com/tapsi-box/utilities-starter")
    licenses {
      license {
        name.set("MIT License")
        url.set("https://opensource.org/licenses/MIT")
        distribution.set("repo")
      }
    }
    developers {
      developer {
        id.set("mahdibohloul")
        name.set("Mahdi Bohloul")
        email.set("mahdiibohloul@gmail.com")
        url.set("https://github.com/mahdibohloul/")
      }
    }
    scm {
      url.set("https://github.com/tapsi-box/utilities-starter")
    }
  }
}

spotless {
  kotlin {
    target("src/**/*.kt")
    ktlint()
      .editorConfigOverride(
        mapOf(
          "indent_size" to 2,
          "ktlint_standard_filename" to "disabled",
          "max_line_length" to "120"
        )
      )
    trimTrailingWhitespace()
    leadingTabsToSpaces()
    endWithNewline()
  }
}

detekt {
  buildUponDefaultConfig = true
  allRules = true
  config.setFrom("$projectDir/detekt.yml")
  baseline = file("$projectDir/detekt-baseline.xml")
}

tasks.register("verifyReadmeContent") {
  val readmeFile = file("README.md")
  val expectedGroup = project.group.toString()
  val expectedVersion = project.version.toString()
  inputs.file(readmeFile)

  doLast {
    val content = readmeFile.readText()

    // List of checks
    val checks = listOf(
      Check("group ID", """<groupId>$expectedGroup</groupId>"""),
      Check("version", """<version>$expectedVersion</version>"""),
    )

    val errors = checks.mapNotNull { check ->
      if (!content.contains(check.expectedValue)) {
        "Missing or incorrect ${check.name}: ${check.expectedValue}"
      } else null
    }

    if (errors.isNotEmpty()) {
      throw GradleException(
        """
                README content verification failed!
                ${errors.joinToString("\n")}
                Please update the README.md with correct values
            """.trimIndent()
      )
    }
  }
}

tasks.check {
  dependsOn("verifyReadmeContent")
}

data class Check(val name: String, val expectedValue: String)

