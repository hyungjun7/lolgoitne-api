import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10" apply false
	kotlin("plugin.jpa") version "1.6.10" apply false
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
	group = "info.isaaclee.lolgoitne"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

subprojects {

	apply(plugin = "java")

	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")

	apply(plugin = "kotlin")
	apply(plugin = "kotlin-spring") //all-open
	apply(plugin = "kotlin-jpa")

	dependencies {
		//spring boot
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		developmentOnly("org.springframework.boot:spring-boot-devtools")

		//kotlin
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

		//DB connect
		runtimeOnly("com.h2database:h2")
		runtimeOnly("mysql:mysql-connector-java")

		//logback
		implementation("net.logstash.logback:logstash-logback-encoder:6.6")

		//lombok
		implementation("org.projectlombok:lombok")

		//mac
		runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.72.Final:osx-aarch_64")

		//test
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.springframework.security:spring-security-test")
	}

	dependencyManagement {
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		}
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	configurations {
		compileOnly {
			extendsFrom(configurations.annotationProcessor.get())
		}
	}
}

//domain 설정
project(":core") {
	dependencies {
		implementation("javax.inject:javax.inject:1")
	}
	val jar: Jar by tasks
	val bootJar: BootJar by tasks

	bootJar.enabled = false
	jar.enabled = true
}

//domain 설정
project(":common") {
	dependencies {
		implementation("io.jsonwebtoken:jjwt-api:0.11.5")
		implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
		implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	}
	val jar: Jar by tasks
	val bootJar: BootJar by tasks

	bootJar.enabled = false
	jar.enabled = true
}

project(":adapter-in") {
	dependencies {
		implementation(project(":core"))
		implementation(project(":common"))
		implementation(project(":adapter-out:http"))
		implementation(project(":adapter-out:file"))
		implementation("org.springframework.boot:spring-boot-starter-security")
	}
	val jar: Jar by tasks
	val bootJar: BootJar by tasks

	bootJar.enabled = true
	jar.enabled = false
}

project(":adapter-out:http") {
	dependencies {
		implementation(project(":core"))
		implementation("org.springframework.boot:spring-boot-starter-webflux")
	}
	val jar: Jar by tasks
	val bootJar: BootJar by tasks

	bootJar.enabled = false
	jar.enabled = true
}

project(":adapter-out:file") {
	dependencies {
		implementation(project(":core"))
	}
	val jar: Jar by tasks
	val bootJar: BootJar by tasks
	
	bootJar.enabled = false
	jar.enabled = true
}
