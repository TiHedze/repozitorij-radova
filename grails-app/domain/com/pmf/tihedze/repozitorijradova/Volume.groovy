package com.pmf.tihedze.repozitorijradova

class Volume {
    UUID id
    String volume
    String issue

    static mapping = {
        table name: 'volumes'
        id column: 'id', type: 'pg-uuid', generator: 'uuid2'
        volume column: 'volume', 'sqlType': 'varchar'
        issue column: 'issue', 'sqlType': 'varchar'
        version false
    }
    static hasMany = [articles: Article]
    static hasOne = [publication: Publication]
}
