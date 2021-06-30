package com.newland.opengl.renderer2

import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.newland.opengl.R
import com.newland.opengl.app.OpenGlApplication
import com.newland.opengl.utils.ResourceUtils
import com.newland.opengl.utils.ShaderUtils
import com.newland.opengl.utils.TextureUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureRender : GLSurfaceView.Renderer {
    private val vertexPosition = floatArrayOf(
        0f, 0f, 0f,
        1f, 1f, 0f,
        -1f, 1f, 0f,
        -1f, -1f, 0f,
        1f, -1f, 0f,
    )
    private val texturePosition = floatArrayOf(
        0.5f, 0.5f,
        1f, 0f,
        0f, 0f,
        0f, 1f,
        1f, 1f
    )
    private val vertexIndex = shortArrayOf(
        0, 1, 2,
        0, 2, 3,
        0, 3, 4,
        0, 4, 1
    )
    private val vertexBuffer =
        ByteBuffer.allocateDirect(vertexPosition.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().put(vertexPosition).position(0)
    private val textureBuffer =
        ByteBuffer.allocateDirect(texturePosition.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().put(texturePosition).position(0)
    private val vertexIndexBuffer =
        ByteBuffer.allocateDirect(vertexIndex.size * 2).order(ByteOrder.nativeOrder())
            .asShortBuffer().put(vertexIndex).position(0)
    private var mProgramId = 0
    private var mMatrixLocation = 0
    private var textureId = 0
    private var mMatrix = FloatArray(16)
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        val vertexShaderId =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_texture_shader))
        val fragmentShaderId =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_texture_shader))
        mProgramId = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        mMatrixLocation = GLES30.glGetUniformLocation(mProgramId, "u_Matrix")
        textureId = TextureUtils.loadTexture(OpenGlApplication.application, R.drawable.main)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
        val aspectRatio =
            if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) {
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUseProgram(mProgramId)
        GLES30.glUniformMatrix4fv(mMatrixLocation, 1, false, mMatrix, 0)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, textureBuffer)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glDrawElements(
            GLES30.GL_TRIANGLES,
            vertexIndex.size,
            GLES30.GL_UNSIGNED_SHORT,
            vertexIndexBuffer
        )

//        GLES30.glBindTexture(mProgramId, 0)
//        GLES30.glDisableVertexAttribArray(0)
//        GLES30.glDisableVertexAttribArray(1)
    }
}