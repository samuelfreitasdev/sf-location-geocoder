plugins {
	id("geocoder.base")
	`java-library`
}

dependencies {
	implementation(project(":geocoder-core"))

	implementation("org.apache.commons:commons-math3:3.6")
//	implementation("org.knowm.xchart:xchart:3.8.8")
	implementation("org.jfree:jfreechart:1.5.5")

	runtimeOnly(libs.slf4j)

	testImplementation(libs.bundles.junit)
}
