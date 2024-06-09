package com.pmf.tihedze

class AuthorUrlMappings {
    static mappings = {
        def controller = [controller: 'author']
        group '/v1',  {
            def versionedController = controller << [namespace: 'v1']
            group '/authors', {
                post "/" (versionedController << [action: 'create'])
                get "/$id" (versionedController << [action: 'getById'])
            }
        }
    }
}
