package com.newland.opengl.app

import android.app.Application

class OpenGlApplication : Application() {
    companion object {
        lateinit var application: OpenGlApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}