import com.gorylenko.GitPropertiesPluginExtension

plugins {
	id("geocoder.base")
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.git.properties)
	alias(libs.plugins.jib)
}

dependencies {

	implementation(project(":geocoder-core"))
	implementation(project(":geocoder-repository"))
	implementation(project(":geocoder-solvers:geocoder-markov-solver"))

	implementation(libs.kotlin.coroutines.reactive)
	implementation(libs.bundles.spring) {
		exclude(module = "jooq")
	}
	implementation(libs.springdoc.openapi)

	implementation(libs.bundles.camel)
	implementation(libs.bundles.shedlock)
	implementation(libs.bundles.jooq)
	implementation(libs.bundles.jackson)

	annotationProcessor(libs.spring.annotations)
	developmentOnly(libs.spring.devtools)
	developmentOnly(libs.spring.compose)

	runtimeOnly(libs.bundles.hawtio)
	runtimeOnly(libs.netty.all)
//	runtimeOnly(libs.netty.resolver.dns.native.macos)
	runtimeOnly(libs.slf4j)
	runtimeOnly(libs.pg.r2dbc)
	runtimeOnly(libs.pg.jdbc)

	testImplementation(libs.kotlin.coroutines.test)
	testImplementation(libs.bundles.spring.test)
	testImplementation(libs.bundles.junit)
	testImplementation(libs.bundles.testcontainers)
}

tasks {
	processResources {
		val webCli = ":geocoder-webcli"
		dependsOn("$webCli:build")
		doLast {
			val resourceDest = layout.buildDirectory.dir("resources/main").get()

			val appProps = project.properties.filterKeys {
				it == "environmentName" || it.startsWith("flyway") || it.startsWith("db")
			}

			copy {
				from("src/main/resources")
				include("**/*.yml")
				filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to appProps)
				into(resourceDest)
				logger.quiet("Replacing properties resources")
			}

			val webCliOrigin = project(webCli).layout.buildDirectory.get()
			val webCliDest = "$resourceDest/public"
			copy {
				from(webCliOrigin)
				into(webCliDest)
			}
			logger.quiet("Cli Resources: move from $webCliOrigin to $webCliDest")
		}
	}
}

configure<GitPropertiesPluginExtension> {
	dotGitDirectory.set(file("${project.rootDir}/.git"))
	dateFormat = "yyyy-MM-dd'T'HH:mmZ"
	dateFormatTimeZone = "GMT"
}
