databaseChangeLog = {

    changeSet(author: "tihedze (generated)", id: "1724071345684-1") {
        createSequence(incrementBy: "1", sequenceName: "hibernate_sequence", startValue: "1")
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-2") {
        createTable(tableName: "articles") {
            column(name: "id", type: "uuid") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "articlesPK")
            }

            column(name: "volume_id", type: "uuid")

            column(name: "title", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "summary", type: "CLOB") {
                constraints(nullable: "false")
            }

            column(name: "summary_search_vector", type: "tsvector")
        }
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-3") {
        createTable(tableName: "articles_authors") {
            column(name: "author_id", type: "uuid") {
                constraints(nullable: "false")
            }

            column(name: "article_id", type: "uuid") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-4") {
        createTable(tableName: "authors") {
            column(name: "id", type: "uuid") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "authorsPK")
            }

            column(name: "first_name", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: "VARCHAR") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-5") {
        createTable(tableName: "authors_articles") {
            column(name: "article_id", type: "uuid") {
                constraints(nullable: "false")
            }

            column(name: "author_id", type: "uuid") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-6") {
        createTable(tableName: "publications") {
            column(name: "id", type: "uuid") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "publicationsPK")
            }

            column(name: "name", type: "VARCHAR") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-7") {
        createTable(tableName: "users") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "usersPK")
            }

            column(name: "password_hash", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "username", type: "VARCHAR") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-8") {
        createTable(tableName: "volumes") {
            column(name: "id", type: "uuid") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "volumesPK")
            }

            column(name: "volume", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "publication_id", type: "uuid")

            column(name: "issue", type: "VARCHAR") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-9") {
        addUniqueConstraint(columnNames: "title", constraintName: "UC_ARTICLESTITLE_COL", tableName: "articles")
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-10") {
        addForeignKeyConstraint(baseColumnNames: "volume_id", baseTableName: "articles", constraintName: "FK6jas4g31f9t12abr948l2nbdr", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "volumes", validate: "true", onDelete: "set null")
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-11") {
        addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "articles_authors", constraintName: "FKd9mi4u0qe7vf2xrtdxgdnkgkv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "authors", validate: "true")
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-12") {
        addForeignKeyConstraint(baseColumnNames: "article_id", baseTableName: "articles_authors", constraintName: "FKfdw48vudwpjcsfypljm4wmon0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "articles", validate: "true")
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-13") {
        addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "authors_articles", constraintName: "FKj4l5hhil1lfvydewru8cdyjgb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "authors", validate: "true")
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-14") {
        addForeignKeyConstraint(baseColumnNames: "publication_id", baseTableName: "volumes", constraintName: "FKl538l10qgswbl2lwv296g6yp9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "publications", validate: "true", onDelete: "set null")
    }

    changeSet(author: "tihedze (generated)", id: "1724071345684-15") {
        addForeignKeyConstraint(baseColumnNames: "article_id", baseTableName: "authors_articles", constraintName: "FKmmxwqa8hq2yrl31l8wy654yo0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "articles", validate: "true")
    }
}
