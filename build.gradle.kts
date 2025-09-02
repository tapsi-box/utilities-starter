plugins {
  kotlin("jvm") version "1.9.23"
  kotlin("plugin.spring") version "1.9.23"
  id("io.spring.dependency-management") version "1.1.7"

  id("com.vanniktech.maven.publish") version "0.34.0"
  id("com.diffplug.spotless") version "7.2.1"
  id("io.gitlab.arturbosch.detekt") version "1.23.6"
  `java-library`
}

group = "box.tapsi.libs"
version = "0.0.9"
description = "utilities-starter"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework:spring-context:6.2.10")
  implementation("org.springframework.boot:spring-boot-autoconfigure:3.5.5")
  implementation("io.projectreactor:reactor-core:3.7.9")
  implementation("org.springframework.security:spring-security-crypto:6.5.3")
  implementation("io.jsonwebtoken:jjwt-api:0.12.5")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
  implementation("io.jsonwebtoken:jjwt-jackson:0.12.5")
  implementation("io.micrometer:micrometer-core:1.15.3")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.3")
  implementation("io.projectreactor.addons:reactor-extra:3.5.2")
  implementation("org.slf4j:slf4j-api:2.0.17")
  implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")

  api("com.appmattus.fixture:fixture:1.2.0")

  testImplementation(platform("org.junit:junit-bom:5.11.0"))
  testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.5")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("io.projectreactor:reactor-test:3.7.9")
  testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
}



kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
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
    description.set("Tapsi Metrics Core - Reactive Observability Library for Spring Boot Applications.")
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
          "ktlint_standard_max-line-length" to "120"
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

