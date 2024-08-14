databaseChangeLog = {

    changeSet(author: "tilen (generated)", id: "1723642483239-1") {
        createSequence(incrementBy: "1", sequenceName: "hibernate_sequence", startValue: "1")
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-2") {
        createTable(tableName: "articles") {
            column(name: "id", type: "char(36)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "articlesPK")
            }

            column(name: "volume_id", type: "char(36)") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "summary", type: "CLOB") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-3") {
        createTable(tableName: "articles_authors") {
            column(name: "author_id", type: "char(36)") {
                constraints(nullable: "false")
            }

            column(name: "article_id", type: "char(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-4") {
        createTable(tableName: "authors") {
            column(name: "id", type: "char(36)") {
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

    changeSet(author: "tilen (generated)", id: "1723642483239-5") {
        createTable(tableName: "authors_articles") {
            column(name: "article_id", type: "char(36)") {
                constraints(nullable: "false")
            }

            column(name: "author_id", type: "char(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-6") {
        createTable(tableName: "publications") {
            column(name: "id", type: "char(36)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "publicationsPK")
            }

            column(name: "name", type: "VARCHAR") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-7") {
        createTable(tableName: "user") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "userPK")
            }

            column(name: "password_hash", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "username", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "publication_id", type: "char(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-8") {
        createTable(tableName: "volume") {
            column(name: "id", type: "char(36)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "volumePK")
            }

            column(name: "volume", type: "VARCHAR") {
                constraints(nullable: "false")
            }

            column(name: "publication_id", type: "char(36)") {
                constraints(nullable: "false")
            }

            column(name: "issue", type: "VARCHAR") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-9") {
        addUniqueConstraint(columnNames: "title", constraintName: "UC_ARTICLESTITLE_COL", tableName: "articles")
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-10") {
        addForeignKeyConstraint(baseColumnNames: "publication_id", baseTableName: "volume", constraintName: "FK5ui7g4ykckm0deh5anqo9ub45", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "publications", validate: "true")
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-11") {
        addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "articles_authors", constraintName: "FKd9mi4u0qe7vf2xrtdxgdnkgkv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "authors", validate: "true")
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-12") {
        addForeignKeyConstraint(baseColumnNames: "article_id", baseTableName: "articles_authors", constraintName: "FKfdw48vudwpjcsfypljm4wmon0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "articles", validate: "true")
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-13") {
        addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "authors_articles", constraintName: "FKj4l5hhil1lfvydewru8cdyjgb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "authors", validate: "true")
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-14") {
        addForeignKeyConstraint(baseColumnNames: "volume_id", baseTableName: "articles", constraintName: "FKjrphj0n5aqqhdd7xja5xrvsi1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "volume", validate: "true")
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-15") {
        addForeignKeyConstraint(baseColumnNames: "article_id", baseTableName: "authors_articles", constraintName: "FKmmxwqa8hq2yrl31l8wy654yo0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "articles", validate: "true")
    }

    changeSet(author: "tilen (generated)", id: "1723642483239-16") {
        addForeignKeyConstraint(baseColumnNames: "publication_id", baseTableName: "user", constraintName: "FKru5g087ue0q3h8w71koga1pt0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "publications", validate: "true")
    }
}
