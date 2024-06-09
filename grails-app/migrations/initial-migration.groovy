databaseChangeLog = {

    changeSet(author: "tilen (generated)", id: "1717941384991-1") {
        createTable(tableName: "articles") {
            column(name: "id", type: "UUID") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "articlesPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "summary", type: "TSVECTOR") {
                constraints(nullable: "false")
            }

            column(name: "publication_id", type: "UUID") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1717941384991-2") {
        createTable(tableName: "authors") {
            column(name: "id", type: "UUID") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "authorsPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "first_name", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: "VARCHAR") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1717941384991-3") {
        createTable(tableName: "authors_articles") {
            column(name: "article_id", type: "UUID") {
                constraints(nullable: "false")
            }

            column(name: "author_id", type: "UUID") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1717941384991-4") {
        createTable(tableName: "publications") {
            column(name: "id", type: "UUID") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "publicationsPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "volume", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "issue", type: "VARCHAR") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1717941384991-5") {
        addPrimaryKey(columnNames: "author_id, article_id", constraintName: "authors_articlesPK", tableName: "authors_articles")
    }

    changeSet(author: "tilen (generated)", id: "1717941384991-6") {
        addUniqueConstraint(columnNames: "title", constraintName: "UC_ARTICLESTITLE_COL", tableName: "articles")
    }

    changeSet(author: "tilen (generated)", id: "1717941384991-7") {
        addForeignKeyConstraint(baseColumnNames: "publication_id", baseTableName: "articles", constraintName: "FKajwxagwbgvxaxc5dndq57e5dk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "publications", validate: "true")
    }

    changeSet(author: "tilen (generated)", id: "1717941384991-8") {
        addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "authors_articles", constraintName: "FKj4l5hhil1lfvydewru8cdyjgb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "authors", validate: "true")
    }

    changeSet(author: "tilen (generated)", id: "1717941384991-9") {
        addForeignKeyConstraint(baseColumnNames: "article_id", baseTableName: "authors_articles", constraintName: "FKmmxwqa8hq2yrl31l8wy654yo0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "articles", validate: "true")
    }
}
