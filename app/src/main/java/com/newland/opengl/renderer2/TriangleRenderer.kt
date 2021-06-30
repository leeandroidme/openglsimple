package com.newland.opengl.renderer2

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.opengl.common.IRendererLife
import com.newland.common.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: leellun
 * @data: 29/6/2021.
 *
 */
class TriangleRenderer : GLSurfaceView.Renderer, IRendererLife {
    private var mProgram = 0
    private val vertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )
    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexPoints.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(vertexPoints)
                position(0)
            }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0f, 0f, 0f)
        val vertexShadeStr = "#version 300 es\n" +
                "layout(location=0) in vec4 vPosition;\n" +
                "void main(){" +
                "gl_Position=vPosition;" +
                "}"
        val fragmentShaderStr = "#version 300 es \n" +
                "precision mediump float;\n" +
                "out vec4 fragColor;\n" +
                "void main(){" +
                "fragColor=vec4(1.0,0,0,1.0);\n" +
                "}"
        val vertexShaderId = ShaderUtils.compileVertexShader(vertexShadeStr)
        val fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShaderStr)
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUseProgram(mProgram)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
        GLES30.glDisableVertexAttribArray(0)
    }

    override fun shudown() {
        GLES30.glDeleteProgram(mProgram)
    }
}