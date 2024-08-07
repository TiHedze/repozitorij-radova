package com.pmf.tihedze.repozitorijradova

import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType
import org.hibernate.annotations.TypeDef

class Article {
    UUID id
    String title
    String summary

    static mapping = {
        table name: 'articles'
        id column: 'id', sqlType: 'pg-uuid', generator: 'uuid2'
        title column: 'title', sqlType: 'varchar'
        summary column: 'summary', sqlType: 'text'
        version false
    }

    static belongsTo = [Publication, Author]
    static hasMany = [authors: Author]
    static hasOne = [publication: Publication]

    static constraints = {
        title unique: true
    }
}
