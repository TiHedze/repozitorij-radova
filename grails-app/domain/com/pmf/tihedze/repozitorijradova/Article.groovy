package com.pmf.tihedze.repozitorijradova

class Article {
    UUID id
    String title
    String summary
    String summarySearchVector

    static mapping = {
        table name: 'articles'
        id column: 'id', sqlType: 'uuid', generator: 'uuid2'
        title column: 'title', sqlType: 'varchar'
        summary column: 'summary', sqlType: 'text'
        summarySearchVector column: 'summary_search_vector', sqlType: 'tsvector'
    }

    static belongsTo = [Publication, Author]
    static hasMany = [authors: Author]
    static hasOne = [publication: Publication]

    static constraints = {
        title unique: true
    }

    def beforeInsert() {
        summarySearchVector = toTsVector(summary)
    }

    def beforeUpdate() {
        summarySearchVector = toTsVector(summary)
    }

    private static String toTsVector(String summary) {
        return Article.withNewSession { session ->
            def result = session.createSQLQuery("SELECT to_tsvector('english', :summary)")
                    .setParameter('summary', summary)
                    .uniqueResult()
            return result.toString()
        }
    }
}
