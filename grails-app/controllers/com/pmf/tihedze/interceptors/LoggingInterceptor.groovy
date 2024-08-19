package com.pmf.tihedze.interceptors

import java.text.SimpleDateFormat

class LoggingInterceptor {
    int order = HIGHEST_PRECEDENCE

    LoggingInterceptor() {
        matchAll()
    }

    @Override
    boolean before() {
        final def now = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date())
        log.info("[$now] Started processing: [$request.method] $request.requestURI")
        true
    }

    @Override
    boolean after() {
        true
    }

    @Override
    void afterView() {
        final def now = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date())
        log.info("[$now] Finished processing: [$request.method] $request.requestURI with result $response.status ")
        true
    }
}
