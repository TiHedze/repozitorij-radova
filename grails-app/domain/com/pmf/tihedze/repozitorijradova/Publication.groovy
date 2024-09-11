package com.pmf.tihedze.repozitorijradova

class Publication {
    UUID id
    String name
    String source

    static mapping = {
        table name: 'publications'
        id column: 'id', type: 'pg-uuid', generator: 'uuid2'
        name column: 'name', sqlType: 'varchar'
        source column: 'source', sqlType: 'varchar'
        version false
    }

    static hasMany = [volumes: Volume]
}
