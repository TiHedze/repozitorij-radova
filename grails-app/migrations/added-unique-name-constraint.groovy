databaseChangeLog = {

    changeSet(author: "tilen (generated)", id: "1718966732885-1") {
        addUniqueConstraint(columnNames: "name", constraintName: "UC_PUBLICATIONSNAME_COL", tableName: "publications")
    }
}
