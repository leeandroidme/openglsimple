package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import com.newland.opengl.R
import com.newland.opengl.utils.ResourceUtils
import com.newland.common.utils.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 *
 */
class UniformRenderer : GLSurfaceView.Renderer {
    private var mProgram = 0
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        //编译
        val vertexShaderId: Int =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_uniform_shader))
        val fragmentShaderId: Int =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_uniform_shader))
        //鏈接程序片段
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        //在OpenGLES环境中使用程序片段
        GLES30.glUseProgram(mProgram)
        val maxUniforms = IntArray(1)
        GLES30.glGetProgramiv(mProgram, GLES30.GL_ACTIVE_UNIFORM_MAX_LENGTH, maxUniforms, 0)
        val numUniforms = IntArray(1)
        GLES30.glGetProgramiv(mProgram, GLES30.GL_ACTIVE_UNIFORMS, numUniforms, 0)
        Log.d(TAG, "maxUniforms=" + maxUniforms[0] + " numUniforms=" + numUniforms[0])
        val length = IntArray(1)
        val size = IntArray(1)
        val type = IntArray(1)
        val nameBuffer = ByteArray(maxUniforms[0] - 1)
        for (index in 0 until numUniforms[0]) {
            GLES30.glGetActiveUniform(
                mProgram,
                index,
                maxUniforms[0],
                length,
                0,
                size,
                0,
                type,
                0,
                nameBuffer,
                0
            )
            val uniformName = String(nameBuffer)
            val location: Int = GLES30.glGetUniformLocation(mProgram, uniformName)
            Log.d(
                TAG,
                "uniformName=" + uniformName + " location=" + location + " type=" + type[0] + " size=" + size[0]
            )
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
    }

    companion object {
        private const val TAG = "UniformRenderer"
    }
}