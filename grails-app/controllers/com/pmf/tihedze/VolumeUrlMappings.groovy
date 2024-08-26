package com.pmf.tihedze

class VolumeUrlMappings {
    static mappings = {
        final def controller = [controller: 'volume']
        group '/v1', {
            group "/volumes", {
                final def versionedController = controller << [namespace: 'v1']
                get "/"(versionedController << [action: 'getByQuery'])
                get "/name"(versionedController << [action: 'getAllByNameQuery'])
                get "/$id"(versionedController << [action: 'getById'])
                get "/publication/$id"(versionedController << [action: 'getAllByPublicationId'])
                post "/"(versionedController << [action: 'create'])
                put "/$id"(versionedController << [action: 'update'])
                delete "/$id"(versionedController << [action: 'delete'])
                post "/$id/add-articles"(versionedController << [action: 'addArticles'])
                post "/$id/remove-articles"(versionedController << [action: 'removeArticles'])
            }
        }
    }
}
