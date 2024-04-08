import net.researchgate.release.ReleaseExtension

plugins {
    base
    id("idea")
    id("com.diffplug.spotless")
    id("net.saliman.properties")
    alias(libs.plugins.release)
}

allprojects {
    group = "com.sf"
    description = "Sample Geocoding Application using Kotlin + Spring Boot + Websockets"
}

repositories {
    mavenLocal()
    mavenCentral()
}

spotless {
    format("misc") {
        target("**/.gitignore", "README.md")
        indentWithTabs()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

configure<ReleaseExtension> {
    tagTemplate.set("v\$version")
    with(git) {
        requireBranch.set("master")
    }
}
