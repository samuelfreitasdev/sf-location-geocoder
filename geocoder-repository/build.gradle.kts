import org.jooq.meta.jaxb.ForcedType

plugins {
	id("geocoder.base")
	alias(libs.plugins.flyway)
	alias(libs.plugins.jooq)
	`java-library`
}

val flywayMigration = configurations.create("flywayMigration")

dependencies {
	implementation(project(":geocoder-core"))
	api(libs.bundles.jooq)

	runtimeOnly(libs.slf4j)
	runtimeOnly(libs.pg.r2dbc)
	jooqGenerator(libs.pg.jdbc)
	flywayMigration(libs.pg.jdbc)
}

tasks.flywayMigrate {
	dependsOn("processResources")
}

flyway {
	configurations = arrayOf("flywayMigration")
}

jooq {
	version.set(libs.versions.jooq.get())
	configurations {
		create("main") {
			jooqConfiguration.apply {
				jdbc.apply {
					driver = project.property("flyway.driver").toString()
					url = project.property("flyway.url").toString()
					user = project.property("flyway.user").toString()
					withPassword(project.property("flyway.password").toString())
					username = project.property("flyway.user").toString()
					password = project.property("flyway.password").toString()
				}
				generator.apply {
					name = "org.jooq.codegen.KotlinGenerator"
					database.apply {
						name = project.property("jooq.generator").toString()
						inputSchema = project.property("flyway.defaultSchema").toString()
						forcedTypes = listOf(
							ForcedType().apply {
								name = "Instant"
								types = "timestamp"
							}
						)
					}
					generate.apply {
						isImplicitJoinPathsAsKotlinProperties = true
						isPojosAsKotlinDataClasses = true
						isKotlinNotNullPojoAttributes = true
						isKotlinNotNullRecordAttributes = true
						isKotlinNotNullInterfaceAttributes = true
						isRoutines = false
						isDeprecated = false
					}
					strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
				}
			}
		}
	}
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
	// ensure database schema has been prepared by Flyway before generating the jOOQ sources
	dependsOn("flywayMigrate")

	// declare Flyway migration scripts as inputs on the jOOQ task
	inputs.files(fileTree("${project.projectDir}/src/main/resources/db/migration"))
		.withPropertyName("migrations")
		.withPathSensitivity(PathSensitivity.RELATIVE)

	// make jOOQ task participate in incremental builds and build caching
	allInputsDeclared.set(true)
	outputs.cacheIf { true }
}
