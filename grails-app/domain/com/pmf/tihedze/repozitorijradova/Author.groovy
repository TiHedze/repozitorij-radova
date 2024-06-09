package com.pmf.tihedze.repozitorijradova

class Author {
    UUID id
    String firstName
    String lastName

    static mapping = {
        table name: 'authors'
        id type: 'pg-uuid', column: 'id', generator: 'uuid2'
        firstName sqlType: 'varchar', column: 'first_name'
        lastName sqlType: 'varchar', column: 'last_name'
    }

    static hasMany = [articles: Article]
}
