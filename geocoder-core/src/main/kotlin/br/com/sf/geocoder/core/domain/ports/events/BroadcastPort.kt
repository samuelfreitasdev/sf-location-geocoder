package br.com.sf.geocoder.core.domain.ports.events

import br.com.sf.geocoder.core.domain.messages.SolutionCommand

interface BroadcastPort {
	fun broadcastSolution(command: SolutionCommand)
}
