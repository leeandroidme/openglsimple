package com.newland.common

import android.app.Application

/**
 * @author: leellun
 * @data: 30/6/2021.
 *
 */
open class OpenBaseApplication : Application() {
    companion object {
        lateinit var application: OpenBaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}