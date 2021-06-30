package com.newland.demo1.renderer

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.common.utils.ShaderUtils
import com.newland.demo1.R
import com.newland.opengl.utils.ResourceUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: leellun
 * @data: 30/6/2021.
 *
 */
class AirHockeyRenderer : GLSurfaceView.Renderer {
    private var mProgram = 0
    private val trianglesCoords = floatArrayOf(
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,

        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,

        // Line 1
        -0.5f, 0f,
        0.5f, 0f,

        // Mallets
        0f, -0.25f,
        0f, 0.25f
    )
    private var aPositionLocation = -1
    private var uColorLocation = -1
    private val trianglesBuffer =
        ByteBuffer.allocateDirect(trianglesCoords.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().also {
                it.put(trianglesCoords)
                it.position(0)
            }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES30.glClearColor(0f, 0f, 0f, 0f)
        val vertexId =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_air_hockey))
        val framentId =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_air_hockey))
        val programId = ShaderUtils.linkProgram(vertexId, framentId)
        mProgram = programId
        GLES30.glUseProgram(mProgram)
        aPositionLocation = GLES30.glGetAttribLocation(mProgram, "a_Position")
        uColorLocation = GLES30.glGetUniformLocation(mProgram, "u_Color")
        GLES30.glVertexAttribPointer(
            aPositionLocation,
            2,
            GLES30.GL_FLOAT,
            false,
            0,
            trianglesBuffer
        )
        GLES30.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUniform4f(uColorLocation, 1f, 1f, 1f, 1f)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6)

        GLES30.glLineWidth(10f)
        GLES30.glUniform4f(uColorLocation, 1f, 0f, 0f, 1f)
        GLES30.glDrawArrays(GLES30.GL_LINES, 6, 2)

        GLES30.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        GLES30.glDrawArrays(GLES30.GL_POINTS, 8, 1)

        GLES30.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES30.glDrawArrays(GLES30.GL_POINTS, 9, 1)
    }
}