package com.pmf.tihedze

import org.springframework.stereotype.Component

@Component
class UrlMappings {
    static mappings = {
        "500"(view:'/error')
        "404"(view:'/notFound')

    }
}
