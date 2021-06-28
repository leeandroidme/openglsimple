package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class BackgroundRender : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景色
        GLES30.glClearColor(0f, 0f, 1f, 1f)
        // Create a minimum supported OpenGL ES context, then check:
        gl?.glGetString(GL10.GL_VERSION).also {
            Log.w("0000000000", "Version: $it")
        }

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置视图窗口
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        //把颜色缓冲区设置为我们预设的颜色
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT)
    }
}