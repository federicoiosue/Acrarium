/*
 * (C) Copyright 2018 Lukas Morawietz (https://github.com/F43nd1r)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.faendir.acra.liquibase

import liquibase.Liquibase
import liquibase.changelog.ChangeSet
import liquibase.changelog.ChangeSet.ExecType
import liquibase.changelog.DatabaseChangeLog
import liquibase.changelog.visitor.AbstractChangeExecListener
import liquibase.database.Database
import liquibase.exception.LiquibaseException
import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component
import java.sql.Connection
import java.util.*
import java.util.function.Consumer
import javax.sql.DataSource

/**
 * @author lukas
 * @since 01.06.18
 */
@Component("liquibase")
@EnableConfigurationProperties(LiquibaseProperties::class)
class ChangeAwareSpringLiquibase constructor(properties: LiquibaseProperties, dataSource: DataSource) : SpringLiquibase() {
    private val processors: MutableList<LiquibaseChangePostProcessor> = mutableListOf()

    init {
        setDataSource(dataSource)
        setChangeLog(properties.changeLog)
        setContexts(properties.contexts)
        setDefaultSchema(properties.defaultSchema)
        isDropFirst = properties.isDropFirst
        setShouldRun(properties.isEnabled)
        setLabels(properties.labels)
        setChangeLogParameters(properties.parameters)
        setRollbackFile(properties.rollbackFile)
    }

    @Autowired(required = false)
    fun setProcessors(processors: List<LiquibaseChangePostProcessor>) {
        this.processors.addAll(processors)
    }

    @Throws(LiquibaseException::class)
    override fun createLiquibase(c: Connection): Liquibase {
        return super.createLiquibase(c).apply {
            setChangeExecListener(object : AbstractChangeExecListener() {
                override fun ran(changeSet: ChangeSet, databaseChangeLog: DatabaseChangeLog, database: Database, execType: ExecType) {
                    processors.forEach { it.handle(changeSet) }
                }
            })
        }
    }
}