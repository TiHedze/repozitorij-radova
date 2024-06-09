package com.pmf.tihedze.repozitorijradova

class Article {
    UUID id
    String title
    String summary

    static mapping = {
        table name: 'articles'
        id column: 'id', sqlType: 'uuid', generator: 'uuid'
        title column: 'title', sqlType: 'varchar'
        summary column: 'summary', sqlType: 'tsvector'
    }

    static belongsTo = [Publication, Author]
    static hasMany = [authors: Author]
    static hasOne = [publication: Publication]

    static constraints = {
        title unique: true
    }
}
