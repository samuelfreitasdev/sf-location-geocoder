/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables


import org.jooq.*
import org.jooq.generated.Public
import org.jooq.generated.keys.GEOCODER_SOLUTION_PKEY
import org.jooq.generated.keys.GEOCODER_SOLUTION__FK_GEOCODER_PROBLEM_ID
import org.jooq.generated.tables.GeocoderProblem.GeocoderProblemPath
import org.jooq.generated.tables.records.GeocoderSolutionRecord
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl
import java.time.Instant


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class GeocoderSolution(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, GeocoderSolutionRecord>?,
    parentPath: InverseForeignKey<out Record, GeocoderSolutionRecord>?,
    aliased: Table<GeocoderSolutionRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<GeocoderSolutionRecord>(
    alias,
    Public.PUBLIC,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>public.geocoder_solution</code>
         */
        val GEOCODER_SOLUTION: GeocoderSolution = GeocoderSolution()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<GeocoderSolutionRecord> = GeocoderSolutionRecord::class.java

    /**
     * The column <code>public.geocoder_solution.geocoder_problem_id</code>.
     */
    val GEOCODER_PROBLEM_ID: TableField<GeocoderSolutionRecord, Long?> = createField(DSL.name("geocoder_problem_id"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>public.geocoder_solution.suggestedcoordinate</code>.
     */
    val SUGGESTEDCOORDINATE: TableField<GeocoderSolutionRecord, JSON?> = createField(DSL.name("suggestedcoordinate"), SQLDataType.JSON.nullable(false), this, "")

    /**
     * The column <code>public.geocoder_solution.created_at</code>.
     */
    val CREATED_AT: TableField<GeocoderSolutionRecord, Instant?> = createField(DSL.name("created_at"), SQLDataType.INSTANT.nullable(false), this, "")

    /**
     * The column <code>public.geocoder_solution.updated_at</code>.
     */
    val UPDATED_AT: TableField<GeocoderSolutionRecord, Instant?> = createField(DSL.name("updated_at"), SQLDataType.INSTANT.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<GeocoderSolutionRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<GeocoderSolutionRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<GeocoderSolutionRecord>?, where: Condition): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.geocoder_solution</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.geocoder_solution</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.geocoder_solution</code> table reference
     */
    constructor(): this(DSL.name("geocoder_solution"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, GeocoderSolutionRecord>?, parentPath: InverseForeignKey<out Record, GeocoderSolutionRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, GEOCODER_SOLUTION, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class GeocoderSolutionPath : GeocoderSolution, Path<GeocoderSolutionRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, GeocoderSolutionRecord>?, parentPath: InverseForeignKey<out Record, GeocoderSolutionRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<GeocoderSolutionRecord>): super(alias, aliased)
        override fun `as`(alias: String): GeocoderSolutionPath = GeocoderSolutionPath(DSL.name(alias), this)
        override fun `as`(alias: Name): GeocoderSolutionPath = GeocoderSolutionPath(alias, this)
        override fun `as`(alias: Table<*>): GeocoderSolutionPath = GeocoderSolutionPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<GeocoderSolutionRecord> = GEOCODER_SOLUTION_PKEY
    override fun getReferences(): List<ForeignKey<GeocoderSolutionRecord, *>> = listOf(GEOCODER_SOLUTION__FK_GEOCODER_PROBLEM_ID)

    private lateinit var _geocoderProblem: GeocoderProblemPath

    /**
     * Get the implicit join path to the <code>public.geocoder_problem</code>
     * table.
     */
    fun geocoderProblem(): GeocoderProblemPath {
        if (!this::_geocoderProblem.isInitialized)
            _geocoderProblem = GeocoderProblemPath(this, GEOCODER_SOLUTION__FK_GEOCODER_PROBLEM_ID, null)

        return _geocoderProblem;
    }

    val geocoderProblem: GeocoderProblemPath
        get(): GeocoderProblemPath = geocoderProblem()
    override fun `as`(alias: String): GeocoderSolution = GeocoderSolution(DSL.name(alias), this)
    override fun `as`(alias: Name): GeocoderSolution = GeocoderSolution(alias, this)
    override fun `as`(alias: Table<*>): GeocoderSolution = GeocoderSolution(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): GeocoderSolution = GeocoderSolution(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): GeocoderSolution = GeocoderSolution(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): GeocoderSolution = GeocoderSolution(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition): GeocoderSolution = GeocoderSolution(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): GeocoderSolution = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition): GeocoderSolution = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>): GeocoderSolution = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): GeocoderSolution = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): GeocoderSolution = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): GeocoderSolution = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): GeocoderSolution = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): GeocoderSolution = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): GeocoderSolution = where(DSL.notExists(select))
}
