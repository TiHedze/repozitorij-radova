databaseChangeLog = {

    changeSet(author: "tihedze (generated)", id: "1718884877278-1") {
        addColumn(tableName: "articles") {
            column(name: "summary_search_vector", type: "tsvector") {
                constraints(nullable: "false")
            }
        }
    }
}
