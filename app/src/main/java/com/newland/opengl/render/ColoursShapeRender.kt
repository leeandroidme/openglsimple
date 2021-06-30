package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.common.OpenBaseApplication
import com.newland.opengl.R
import com.newland.opengl.utils.ResourceUtils
import com.newland.common.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 点、线、三角形
 */
class ColoursShapeRender : GLSurfaceView.Renderer {
    companion object {
        //一个Float占用4Byte
        private const val BYTES_PRE_FLOAT = 4

        //三个顶点
        private const val POSITION_COMPONENT_COUNT = 3
    }

    //顶点位置缓存
    private lateinit var vertexButter: FloatBuffer

    //顶点颜色缓存
    private lateinit var colorBuffer: FloatBuffer

    //渲染程序
    private var mProgram: Int = -1

    //三个顶点的位置参数
    private val triangleCoords = floatArrayOf(
        0.5f, 0.5f, 0.0f, // top
        -0.5f, -0.5f, 0.0f, // bottom left
        0.5f, -0.5f, 0.0f // bottom right
    )

    //三个顶点的颜色参数
    private val colorCoords = floatArrayOf(
        1f, 0f, 0f, 1f,//top
        0f, 1f, 0f, 1f,//bottom left
        0f, 0f, 1f, 1f//bottom right
    )

    init {
        //定点位置 直接分配内存 float每个4个字节
        vertexButter = ByteBuffer.allocateDirect(triangleCoords.size * BYTES_PRE_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexButter.put(triangleCoords)
        vertexButter.position(0)

        //定点颜色分配内存
        colorBuffer = ByteBuffer.allocateDirect(colorCoords.size * BYTES_PRE_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        colorBuffer.put(colorCoords)
        colorBuffer.position(0)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        //背景设置为白色
        GLES30.glClearColor(1f, 1f, 1f, 1f)
        var vertexShaderStr =
            ResourceUtils.readResource(OpenBaseApplication.application, R.raw.vertex_simple_shade)
        //编译顶点着色程序
        var vertexShaderId = ShaderUtils.compileVertexShader(vertexShaderStr)
        var fragmentShaderStr =
            ResourceUtils.readResource(OpenBaseApplication.application, R.raw.fragment_simple_shade)
        //编译片段着色程序
        var fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShaderStr)
        mProgram = ShaderUtils.likeProgram(vertexShaderId, fragmentShaderId)
        //在OpenGLES环境中使用程序
        GLES30.glUseProgram(mProgram)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        //把颜色缓冲区设置为预设的颜色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        //准备坐标数据
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexButter)
        //启用顶点位置句柄
        GLES30.glEnableVertexAttribArray(0)

        //准备颜色数据
        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 0, colorBuffer)
        //启用顶点颜色句柄
        GLES30.glEnableVertexAttribArray(1)

        //绘制三个点
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, POSITION_COMPONENT_COUNT)
        //线宽
        GLES30.glLineWidth(3F)
        //绘制三条线
        GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 0, POSITION_COMPONENT_COUNT)
        //绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT)

        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(0)
        GLES30.glDisableVertexAttribArray(1)
    }
}



























