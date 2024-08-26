package com.pmf.tihedze

class LoginUrlMappings {
    static mappings = {
        final def controller = [controller: 'login']
        group '/v1',  {
            final def versionedController = controller << [namespace: 'v1']
            group '/auth', {
                post "/register" (versionedController << [action: 'register'])
                post "/login" (versionedController << [action: 'login'])
            }
        }
    }
}
