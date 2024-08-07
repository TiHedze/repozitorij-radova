package com.pmf.tihedze.repozitorijradova

class Publication {
    UUID id
    String name
    String volume
    String issue

    static mapping = {
        table name: 'publications'
        id column: 'id', sqlType: 'pg-uuid', generator: 'uuid2'
        name column: 'name', sqlType: 'varchar', unique: ['volume', 'issue']
        volume column: 'volume', sqlType: 'varchar'
        issue column: 'issue', sqlType: 'varchar'
        version false
    }

    static hasMany = [articles: Article]
}
