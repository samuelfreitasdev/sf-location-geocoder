import { components } from './generated/api'

type GeocoderProblem = components['schemas']['GeocoderProblem']
type GeocoderSolution = components['schemas']['GeocoderSolution']
type GeocoderSolutionRequest = components['schemas']['GeocoderSolutionRequest']
type GeocoderRequest = components['schemas']['GeocoderRequest']
type GeocoderSummary = components['schemas']['GeocoderSummary']
type Coordinate = components['schemas']['Coordinate']

type EditableProblem = {
	id: number
	name: string
	points: Coordinate[]
}

type Page<T> = {
	content: T[]
	totalPages: number
	totalElements: number
	numberOfElements: number
	number: number
	size: number
	first: boolean
	last: boolean
	empty: boolean
}

export type {
	GeocoderProblem,
	EditableProblem,
	GeocoderSolution,
	GeocoderSolutionRequest,
	GeocoderRequest,
	GeocoderSummary,
	Coordinate,
	Page,
}
