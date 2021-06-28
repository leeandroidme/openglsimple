package com.newland.opengl.render

import android.graphics.Color
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: leellun
 * @data: 28/6/2021.
 *
 */
@Suppress("KotlinJniMissingFunction")
class BackgroundNativeRenderer : GLSurfaceView.Renderer {
    companion object{
        init {
            System.loadLibrary("native-lib")
        }
    }
    external fun surfaceCreated(color: Int)
    external fun surfaceChanged(width: Int, height: Int)
    external fun drawFrame()
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        surfaceCreated(Color.BLUE)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        surfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        drawFrame()
    }
}