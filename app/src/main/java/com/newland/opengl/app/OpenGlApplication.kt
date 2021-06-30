package com.newland.opengl.app

import com.newland.common.OpenBaseApplication


/**
 * @author: leellun
 * @data: 30/6/2021.
 *
 */
class OpenGlApplication : com.newland.common.OpenBaseApplication() {
    companion object {
        lateinit var application: OpenBaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}