/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records


import org.jooq.JSON
import org.jooq.Record1
import org.jooq.generated.tables.GeocoderSolution
import org.jooq.impl.UpdatableRecordImpl
import java.time.Instant


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class GeocoderSolutionRecord private constructor() : UpdatableRecordImpl<GeocoderSolutionRecord>(GeocoderSolution.GEOCODER_SOLUTION) {

    open var geocoderProblemId: Long
        set(value): Unit = set(0, value)
        get(): Long = get(0) as Long

    open var suggestedcoordinate: JSON
        set(value): Unit = set(1, value)
        get(): JSON = get(1) as JSON

    open var createdAt: Instant
        set(value): Unit = set(2, value)
        get(): Instant = get(2) as Instant

    open var updatedAt: Instant
        set(value): Unit = set(3, value)
        get(): Instant = get(3) as Instant

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    /**
     * Create a detached, initialised GeocoderSolutionRecord
     */
    constructor(geocoderProblemId: Long, suggestedcoordinate: JSON, createdAt: Instant, updatedAt: Instant): this() {
        this.geocoderProblemId = geocoderProblemId
        this.suggestedcoordinate = suggestedcoordinate
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        resetChangedOnNotNull()
    }
}
