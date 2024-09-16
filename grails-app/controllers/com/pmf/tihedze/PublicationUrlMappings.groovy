package com.pmf.tihedze

class PublicationUrlMappings {
    static mappings = {
        final def controller = [controller: 'publication']
        group '/v1', {
            final def versionedController = controller << [namespace: 'v1']
            group '/publications', {
                get "/"(versionedController << [action: 'getAll'])
                get "/$id"(versionedController << [action: 'getById'])
                post "/"(versionedController << [action: 'create'])
                put "/$id"(versionedController << [action: 'update'])
                delete "/$id"(versionedController << [action: 'delete'])
                get "/populate"(versionedController << [action: 'populateDatabase'])
            }
        }
    }
}
