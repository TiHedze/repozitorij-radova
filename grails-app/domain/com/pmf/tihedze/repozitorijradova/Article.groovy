package com.pmf.tihedze.repozitorijradova

class Article {
    UUID id
    String title
    String summary
    String url
    int year

    static mapping = {
        table name: 'articles'
        id column: 'id', type: 'pg-uuid', generator: 'uuid2'
        title column: 'title', sqlType: 'varchar'
        summary column: 'summary', sqlType: 'text'
        url column: 'url', sqlType: 'varchar'
        year column: 'year', sqlType: 'integer'
        version false
    }

    static hasMany = [authors: Author]
    static hasOne = [volume: Volume]
    static belongsTo= [Volume]

    static constraints = {
        title unique: true
        volume nullable: true
    }
}
