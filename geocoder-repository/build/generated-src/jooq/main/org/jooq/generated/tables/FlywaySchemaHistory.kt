/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables


import org.jooq.*
import org.jooq.generated.Public
import org.jooq.generated.indexes.FLYWAY_SCHEMA_HISTORY_S_IDX
import org.jooq.generated.keys.FLYWAY_SCHEMA_HISTORY_PK
import org.jooq.generated.tables.records.FlywaySchemaHistoryRecord
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl
import java.time.Instant


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class FlywaySchemaHistory(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, FlywaySchemaHistoryRecord>?,
    parentPath: InverseForeignKey<out Record, FlywaySchemaHistoryRecord>?,
    aliased: Table<FlywaySchemaHistoryRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<FlywaySchemaHistoryRecord>(
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
         * The reference instance of <code>public.flyway_schema_history</code>
         */
        val FLYWAY_SCHEMA_HISTORY: FlywaySchemaHistory = FlywaySchemaHistory()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<FlywaySchemaHistoryRecord> = FlywaySchemaHistoryRecord::class.java

    /**
     * The column <code>public.flyway_schema_history.installed_rank</code>.
     */
    val INSTALLED_RANK: TableField<FlywaySchemaHistoryRecord, Int?> = createField(DSL.name("installed_rank"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>public.flyway_schema_history.version</code>.
     */
    val VERSION: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("version"), SQLDataType.VARCHAR(50), this, "")

    /**
     * The column <code>public.flyway_schema_history.description</code>.
     */
    val DESCRIPTION: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("description"), SQLDataType.VARCHAR(200).nullable(false), this, "")

    /**
     * The column <code>public.flyway_schema_history.type</code>.
     */
    val TYPE: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("type"), SQLDataType.VARCHAR(20).nullable(false), this, "")

    /**
     * The column <code>public.flyway_schema_history.script</code>.
     */
    val SCRIPT: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("script"), SQLDataType.VARCHAR(1000).nullable(false), this, "")

    /**
     * The column <code>public.flyway_schema_history.checksum</code>.
     */
    val CHECKSUM: TableField<FlywaySchemaHistoryRecord, Int?> = createField(DSL.name("checksum"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.flyway_schema_history.installed_by</code>.
     */
    val INSTALLED_BY: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("installed_by"), SQLDataType.VARCHAR(100).nullable(false), this, "")

    /**
     * The column <code>public.flyway_schema_history.installed_on</code>.
     */
    val INSTALLED_ON: TableField<FlywaySchemaHistoryRecord, Instant?> = createField(DSL.name("installed_on"), SQLDataType.INSTANT.nullable(false).defaultValue(DSL.field(DSL.raw("now()"), SQLDataType.INSTANT)), this, "")

    /**
     * The column <code>public.flyway_schema_history.execution_time</code>.
     */
    val EXECUTION_TIME: TableField<FlywaySchemaHistoryRecord, Int?> = createField(DSL.name("execution_time"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>public.flyway_schema_history.success</code>.
     */
    val SUCCESS: TableField<FlywaySchemaHistoryRecord, Boolean?> = createField(DSL.name("success"), SQLDataType.BOOLEAN.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<FlywaySchemaHistoryRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<FlywaySchemaHistoryRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<FlywaySchemaHistoryRecord>?, where: Condition): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.flyway_schema_history</code> table
     * reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.flyway_schema_history</code> table
     * reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.flyway_schema_history</code> table reference
     */
    constructor(): this(DSL.name("flyway_schema_history"), null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIndexes(): List<Index> = listOf(FLYWAY_SCHEMA_HISTORY_S_IDX)
    override fun getPrimaryKey(): UniqueKey<FlywaySchemaHistoryRecord> = FLYWAY_SCHEMA_HISTORY_PK
    override fun `as`(alias: String): FlywaySchemaHistory = FlywaySchemaHistory(DSL.name(alias), this)
    override fun `as`(alias: Name): FlywaySchemaHistory = FlywaySchemaHistory(alias, this)
    override fun `as`(alias: Table<*>): FlywaySchemaHistory = FlywaySchemaHistory(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): FlywaySchemaHistory = FlywaySchemaHistory(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): FlywaySchemaHistory = FlywaySchemaHistory(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): FlywaySchemaHistory = FlywaySchemaHistory(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition): FlywaySchemaHistory = FlywaySchemaHistory(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): FlywaySchemaHistory = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition): FlywaySchemaHistory = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>): FlywaySchemaHistory = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): FlywaySchemaHistory = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): FlywaySchemaHistory = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): FlywaySchemaHistory = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): FlywaySchemaHistory = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): FlywaySchemaHistory = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): FlywaySchemaHistory = where(DSL.notExists(select))
}
