plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.5"
	id("com.epages.restdocs-api-spec") version "0.19.4"
	id("com.diffplug.spotless") version "6.25.0"
	id("org.asciidoctor.jvm.convert") version "4.0.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val asciidoctorExt = "asciidoctorExt"
configurations.create(asciidoctorExt) {
	extendsFrom(configurations.testImplementation.get())
}

postman {
	baseUrl = "http://localhost:8080"
}

dependencies {
	// init
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation ("org.springframework.boot:spring-boot-starter-web")
	compileOnly ("org.projectlombok:lombok")
	annotationProcessor ("org.projectlombok:lombok")
	annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
	implementation ("org.springframework.boot:spring-boot-starter-validation")

	// thymeleaf
	implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")

	// database
	implementation ("org.springframework.boot:spring-boot-starter-data-redis")
	runtimeOnly("org.postgresql:postgresql")
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")

	// mail
	implementation ("org.springframework.boot:spring-boot-starter-mail")

	// xss
	implementation("org.jsoup:jsoup:1.18.3")

	// security
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")

	// querydsl
	implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
	annotationProcessor("com.querydsl:querydsl-apt:5.1.0:jakarta")
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")
	implementation("com.querydsl:querydsl-sql:5.1.0")
	implementation("com.blazebit:blaze-persistence-integration-querydsl-expressions-jakarta:1.6.14")

	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.2")

	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
	testImplementation("org.testcontainers:jdbc")
	testImplementation("org.springframework.security:spring-security-test")

	implementation(project(":processor"))
	annotationProcessor(project(":processor"))
	testImplementation("org.reflections:reflections:0.10.2")

}
val snippetsDir by extra { file("${layout.buildDirectory.get()}/generated-snippets") }
val docsOutputDir by extra { file("${layout.buildDirectory.get()}/docs/asciidoc") }

tasks {
	bootJar {
		dependsOn(asciidoctor)
		from(docsOutputDir) {
			into("BOOT-INF/classes/static/docs")
		}

		from("src/docs/asciidoc") {
			include("docinfo.html", "css/")
			into("BOOT-INF/classes/static/docs") // static 디렉토리로 복사
		}

	}

	asciidoctor {
		dependsOn("asciidoctorCleanup")
		dependsOn(test)
		dependsOn("openapi3", "postman")

		configurations("asciidoctorExt")
		baseDirFollowsSourceFile()

		attributes(
			mapOf(
				"snippets" to snippetsDir
			)
		)

		resources {
			from("${rootDir}/src/docs/js") {
				into("js")
			}

			from("${rootDir}/src/docs/css") {
				into("css")
			}
		}
	}

	test {
		useJUnitPlatform()
		outputs.dir(snippetsDir)
	}
}

tasks.register("asciidoctorCleanup", Delete::class) {
	group = "documentation"
	description = "Cleanup Asciidoctor generated files."

	delete(snippetsDir)
	delete(docsOutputDir)

	outputs.upToDateWhen {
		!snippetsDir.exists()
	}
}

spotless {
	isEnforceCheck = false
	java {
		eclipse().configFile("${rootDir}/findy-java-formatter-eclipse.xml")
	}
}