databaseChangeLog = {

    changeSet(author: "tilen (generated)", id: "1719184515116-1") {
        addUniqueConstraint(columnNames: "issue, volume, name", constraintName: "UKb9c9851d21107a464d7a732290cf", tableName: "publications")
    }

    changeSet(author: "tilen (generated)", id: "1719184515116-2") {
        dropUniqueConstraint(constraintName: "uc_publicationsname_col", tableName: "publications")
    }

    changeSet(author: "tilen (generated)", id: "1719184515116-4") {
        dropColumn(columnName: "version", tableName: "articles")
    }

    changeSet(author: "tilen (generated)", id: "1719184515116-5") {
        dropColumn(columnName: "version", tableName: "authors")
    }

    changeSet(author: "tilen (generated)", id: "1719184515116-6") {
        dropColumn(columnName: "version", tableName: "publications")
    }
}
