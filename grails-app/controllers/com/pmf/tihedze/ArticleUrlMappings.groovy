package com.pmf.tihedze

class ArticleUrlMappings {
    static mappings = {
        def controller = [controller: 'article']
        group '/v1', {
            group "/articles", {
                def versionedController = controller << [namespace: 'v1']
                get "/"(versionedController << [action: 'getAll'])
                get "/$id"(versionedController << [action: 'getById'])
                post "/"(versionedController << [action: 'create'])
                put "/$id"(versionedController << [action: 'update'])
                delete "/$id"(versionedController << [action: 'delete'])
            }
        }
    }
}
