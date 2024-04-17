package br.com.sf.geocoder.config.hz

import br.com.sf.geocoder.core.domain.messages.RequestSolverCommand
import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import com.hazelcast.nio.serialization.compact.CompactReader
import com.hazelcast.nio.serialization.compact.CompactSerializer
import com.hazelcast.nio.serialization.compact.CompactWriter
import java.util.*

class RequestSolverCommandSerializer : CompactSerializer<RequestSolverCommand> {
	override fun read(reader: CompactReader): RequestSolverCommand {
		val detailedSolution = reader.readCompact<GeocoderSolution>("detailedSolution")!!
		val solverKey = reader.readString("solverKey")
		val solverName = reader.readString("solverName")
		return RequestSolverCommand(detailedSolution, UUID.fromString(solverKey), solverName!!)
	}

	override fun write(writer: CompactWriter, cmd: RequestSolverCommand) {
		writer.writeCompact("detailedSolution", cmd.solution)
		writer.writeString("solverKey", cmd.solverKey.toString())
		writer.writeString("solverName", cmd.solverName)
	}

	override fun getTypeName(): String {
		return "RequestSolverCommand"
	}

	override fun getCompactClass(): Class<RequestSolverCommand> {
		return RequestSolverCommand::class.java
	}
}
