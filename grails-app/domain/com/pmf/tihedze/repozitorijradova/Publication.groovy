package com.pmf.tihedze.repozitorijradova

class Publication {
    UUID id
    String name
    String volume
    String issue

    static mapping = {
        table name: 'publications'
        id column: 'id', sqlType: 'uuid', generator: 'uuid'
        name column: 'name', sqlType: 'varchar'
        volume column: 'volume', sqlType: 'varchar'
        issue column: 'issue', sqlType: 'varchar'
    }

    static hasMany = [articles: Article]
}
