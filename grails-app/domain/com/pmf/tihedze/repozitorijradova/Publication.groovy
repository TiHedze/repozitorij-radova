package com.pmf.tihedze.repozitorijradova

class Publication {
    UUID id
    String name

    static mapping = {
        table name: 'publications'
        id column: 'id', type: 'pg-uuid', generator: 'uuid2'
        name column: 'name', sqlType: 'varchar'
        issue column: 'issue', sqlType: 'varchar'
        version false
    }

    static hasMany = [volumes: Volume, users: User]
}
