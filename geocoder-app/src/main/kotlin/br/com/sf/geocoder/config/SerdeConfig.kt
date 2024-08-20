package br.com.sf.geocoder.config

import br.com.sf.geocoder.core.serialization.Serde
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Type

@Configuration
class SerdeConfig {
	@Bean
	fun serde(objectMapper: ObjectMapper) =
		object : Serde {
			override fun <T : Any> fromJson(
				content: String,
				type: Type,
			): T {
				return objectMapper.readValue(content, objectMapper.constructType(type))
			}

			override fun toJson(value: Any): String {
				return objectMapper.writeValueAsString(value)
			}
		}
}
