/**
 * Precompiled [geocoder.base.gradle.kts][Geocoder_base_gradle] script plugin.
 *
 * @see Geocoder_base_gradle
 */
public
class Geocoder_basePlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Geocoder_base_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
