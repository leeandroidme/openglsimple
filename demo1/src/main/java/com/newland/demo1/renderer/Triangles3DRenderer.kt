package com.newland.demo1.renderer

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.opengl.Matrix.multiplyMM
import com.newland.common.utils.ShaderUtils
import com.newland.demo1.R
import com.newland.demo1.utils.MatrixHelper.perspectiveM
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
class Triangles3DRenderer : GLSurfaceView.Renderer {
    private val tableTriangles = floatArrayOf(
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 1f, 0f, 0f,

        // Mallets
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
    )
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val tableBuffer =
        ByteBuffer.allocateDirect(tableTriangles.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(tableTriangles)
                position(0)
            }
    private var mProgramId = 0
    private var uMatrixPosition = 0
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0f, 0f, 0f, 0f)
        val vertexId =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_table))
        val framentId =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_air_hockey))
        val programId = ShaderUtils.linkProgram(vertexId, framentId)
        mProgramId = programId
        GLES30.glUseProgram(mProgramId)
        tableBuffer.position(0)
        GLES30.glVertexAttribPointer(
            0,
            2,
            GLES30.GL_FLOAT,
            false,
            5 * 4,
            tableBuffer
        )
        GLES30.glEnableVertexAttribArray(0)
        tableBuffer.position(2)
        GLES30.glVertexAttribPointer(
            1,
            3,
            GLES30.GL_FLOAT,
            false,
            5 * 4,
            tableBuffer
        )
        GLES30.glEnableVertexAttribArray(1)
        uMatrixPosition = GLES30.glGetUniformLocation(mProgramId, "u_Matrix")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
        perspectiveM(
            projectionMatrix, 45f, width.toFloat()
                    / height.toFloat(), 1f, 10f
        )
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f)
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
        multiplyMM(multiplMatrix, 0, projectionMatrix, 0, modelMatrix, 0)
    }

    private var multiplMatrix: FloatArray=FloatArray(16)

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // Assign the matrix
        GLES30.glUniformMatrix4fv(uMatrixPosition, 1, false, projectionMatrix, 0);

        // Draw the table.
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 6);

        // Draw the center dividing line.
        GLES30.glDrawArrays(GLES30.GL_LINES, 6, 2);

        // Draw the first mallet.
        GLES30.glDrawArrays(GLES30.GL_POINTS, 8, 1);

        // Draw the second mallet.
        GLES30.glDrawArrays(GLES30.GL_POINTS, 9, 1);
    }
}