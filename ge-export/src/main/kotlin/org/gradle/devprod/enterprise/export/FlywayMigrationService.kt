package org.gradle.devprod.enterprise.export

import org.flywaydb.core.Flyway
import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class FlywayMigrationService(private val dataSource: DataSource) {

    fun migrateDatabase() {
        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .createSchemas(false)
            .load()
        flyway.migrate()
    }

}