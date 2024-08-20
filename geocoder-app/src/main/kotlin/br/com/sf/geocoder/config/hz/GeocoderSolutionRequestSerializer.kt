package br.com.sf.geocoder.config.hz

import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.domain.model.GeocoderSolutionRequest
import br.com.sf.geocoder.core.domain.model.SolverStatus
import com.hazelcast.nio.serialization.compact.CompactReader
import com.hazelcast.nio.serialization.compact.CompactSerializer
import com.hazelcast.nio.serialization.compact.CompactWriter
import java.util.UUID

class GeocoderSolutionRequestSerializer : CompactSerializer<GeocoderSolutionRequest> {
	override fun read(reader: CompactReader): GeocoderSolutionRequest {
		val solution = reader.readCompact<GeocoderSolution>("solution")!!
		val status = reader.readString("status")!!
		val solverKey = reader.readString("solverKey")
		return GeocoderSolutionRequest(solution, SolverStatus.valueOf(status), UUID.fromString(solverKey))
	}

	override fun write(
		writer: CompactWriter,
		request: GeocoderSolutionRequest,
	) {
		writer.writeCompact("solution", request.solution)
		writer.writeString("status", request.status.toString())
		writer.writeString("solverKey", request.solverKey?.toString())
	}

	override fun getTypeName(): String {
		return "GeocoderSolutionRequest"
	}

	override fun getCompactClass(): Class<GeocoderSolutionRequest> {
		return GeocoderSolutionRequest::class.java
	}
}
