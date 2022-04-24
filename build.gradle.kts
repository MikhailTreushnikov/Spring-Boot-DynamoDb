import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10"
}

group = "com.dynamoDb"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	// https://mvnrepository.com/artifact/io.github.boostchicken/spring-data-dynamodb
	implementation("io.github.boostchicken:spring-data-dynamodb:5.2.5")
	// https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-core
	implementation("com.amazonaws:aws-java-sdk-core:1.12.167")
	// https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-dynamodb
	implementation("com.amazonaws:aws-java-sdk-dynamodb:1.12.167")
	// https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-sts
	implementation("com.amazonaws:aws-java-sdk-sts:1.12.167")


	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
