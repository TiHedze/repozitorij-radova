databaseChangeLog = {
    include file: 'initial-migration.groovy'
    include file: 'add-summary_search_vector.groovy'
    include file: 'added-unique-name-constraint.groovy'
    include file: 'modified-unique-constraints-on-publications.groovy'
}
