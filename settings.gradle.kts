plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "sf-location-geocoder"

include("geocoder-webcli")

include("geocoder-core")
include("geocoder-app")
include("geocoder-repository")
include("geocoder-solver")
