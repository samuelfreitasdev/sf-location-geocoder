plugins {
	id("geocoder.base")
	`java-library`
}

dependencies {
	implementation(project(":geocoder-core"))
//	api(libs.jsprit.core)

	runtimeOnly(libs.slf4j)
}
