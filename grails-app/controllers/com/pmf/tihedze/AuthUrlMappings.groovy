package com.pmf.tihedze

class AuthUrlMappings {
    static mappings = {
        def controller = [controller: 'auth']
        group '/v1',  {
            def versionedController = controller << [namespace: 'v1']
            group '/auth', {
                post "/register" (versionedController << [action: 'register'])
                post "/login" (versionedController << [action: 'login'])
            }
        }
    }
}
