import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.31"
	kotlin("plugin.spring") version "1.4.31"
	kotlin("kapt") version "1.4.31"
	id("nu.studer.jooq") version "5.2"
}

group = "org.gradle.boot-test"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-jooq")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("org.flywaydb:flyway-core:7.7.1")

	runtimeOnly("org.postgresql:postgresql")

	kapt("org.springframework.boot:spring-boot-configuration-processor")

	jooqGenerator("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

jooq {
	configurations {
		create("main") {
			generateSchemaSourceOnCompilation.set(false)
			jooqConfiguration.apply {
				jdbc.apply {
					driver = "org.postgresql.Driver"
					url = "jdbc:postgresql://localhost:5432/btdevprod"
					user = "btdevprod"
					password = "btdevprod"
				}
				generator.apply {
					name = "org.jooq.codegen.DefaultGenerator"
					database.apply {
						name = "org.jooq.meta.postgres.PostgresDatabase"
						inputSchema = "public"
						excludes = "flyway_schema_history"
					}
					target.apply {
						packageName = "org.gradle.devprod.enterprise.export.generated.jooq"
						directory = "src/main/java"
					}
					strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
				}
			}
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
