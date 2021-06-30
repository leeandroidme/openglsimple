package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.opengl.R
import com.newland.opengl.utils.ResourceUtils
import com.newland.common.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * map
 */
class MapBufferRenderer : GLSurfaceView.Renderer {
    private var mProgram = 0

    /**
     * 点的坐标
     */
    private val vertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )
    private val vboIds = IntArray(1)
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        //编译
        val vertexShaderId: Int =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_map_buffer_shader))
        val fragmentShaderId: Int =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_map_buffer_shader))
        //鏈接程序片段
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)

        //1. 生成1个缓冲ID
        GLES30.glGenBuffers(1, vboIds, 0)
        //2. 向顶点坐标数据缓冲送入数据把顶点数组复制到缓冲中
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboIds[0])
        //创建并初始化缓冲区对象的数据存储
        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER,
            vertexPoints.size * 4,
            null,
            GLES30.GL_STATIC_DRAW
        )

        //3. 映射缓冲区对象
        val buffer = GLES30.glMapBufferRange(
            GLES30.GL_ARRAY_BUFFER,
            0,
            vertexPoints.size * 4,
            GLES30.GL_MAP_WRITE_BIT or GLES30.GL_MAP_INVALIDATE_BUFFER_BIT
        ) as ByteBuffer
        //4. 填充数据
        buffer.order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexPoints).position(0)

        //5. 将顶点位置数据送入渲染管线
        GLES30.glVertexAttribPointer(
            VERTEX_POS_INDEX,
            VERTEX_POS_SIZE,
            GLES30.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            0
        )
        //启用顶点位置属性
        GLES30.glEnableVertexAttribArray(VERTEX_POS_INDEX)

        //解除映射
        GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        //使用程序片段
        GLES30.glUseProgram(mProgram)

        //6. 开始绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    companion object {
        private const val TAG = "VertexBufferRenderer"
        private const val VERTEX_POS_INDEX = 0
        private const val VERTEX_POS_SIZE = 3
        private const val VERTEX_STRIDE = VERTEX_POS_SIZE * 4
    }
}