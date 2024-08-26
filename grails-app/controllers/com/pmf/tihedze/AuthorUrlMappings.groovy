package com.pmf.tihedze

class AuthorUrlMappings {
    static mappings = {
        final def controller = [controller: 'author']
        group '/v1',  {
            final def versionedController = controller << [namespace: 'v1']
            group '/authors', {
                post "/" (versionedController << [action: 'create'])
                get "/$id" (versionedController << [action: 'getById'])
                get "/" (versionedController << [action: 'getAll'])
                delete "/$id" (versionedController << [action: 'delete'])
                put "/$id" (versionedController << [action: 'update'])
            }
        }
    }
}
