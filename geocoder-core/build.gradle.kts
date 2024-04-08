plugins {
	id("geocoder.base")
	`java-library`
}

dependencies {
	api(libs.kotlin.coroutines.core)

	runtimeOnly(libs.slf4j)

	testImplementation(libs.kotlin.coroutines.test)
}
