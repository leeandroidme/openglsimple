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
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureRender2:GLSurfaceView.Renderer {
    private val TAG = "TextureRenderer"

    private var vertexBuffer: FloatBuffer? =null
    private  var mTexVertexBuffer: java.nio.FloatBuffer? = null

    private var mVertexIndexBuffer: ShortBuffer? = null

    private var mProgram = 0

    private var textureId = 0

    /**
     * 顶点坐标
     * (x,y,z)
     */
    private val POSITION_VERTEX = floatArrayOf(
        0f, 0f, 0f,  //顶点坐标V0
        1f, 1f, 0f,  //顶点坐标V1
        -1f, 1f, 0f,  //顶点坐标V2
        -1f, -1f, 0f,  //顶点坐标V3
        1f, -1f, 0f //顶点坐标V4
    )

    /**
     * 纹理坐标
     * (s,t)
     */
    private val TEX_VERTEX = floatArrayOf(
        0.5f, 0.5f,  //纹理坐标V0
        1f, 0f,  //纹理坐标V1
        0f, 0f,  //纹理坐标V2
        0f, 1f,  //纹理坐标V3
        1f, 1f //纹理坐标V4
    )

    /**
     * 索引
     */
    private val VERTEX_INDEX = shortArrayOf(
        0, 1, 2,  //V0,V1,V2 三个顶点组成一个三角形
        0, 2, 3,  //V0,V2,V3 三个顶点组成一个三角形
        0, 3, 4,  //V0,V3,V4 三个顶点组成一个三角形
        0, 4, 1 //V0,V4,V1 三个顶点组成一个三角形
    )

    private var uMatrixLocation = 0

    private val mMatrix = FloatArray(16)
    init {
        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(POSITION_VERTEX.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        //传入指定的坐标数据
        vertexBuffer?.put(POSITION_VERTEX)
        vertexBuffer?.position(0)
        mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(TEX_VERTEX)
        mTexVertexBuffer?.position(0)
        mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(VERTEX_INDEX)
        mVertexIndexBuffer?.position(0)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        //编译
        val vertexShaderId: Int =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_texture_shader))
        val fragmentShaderId: Int =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_texture_shader))
        //链接程序片段
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix")
        //加载纹理
        textureId = TextureUtils.loadTexture(OpenGlApplication.application, R.drawable.main)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val aspectRatio =
            if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) {
            //横屏
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -2f, 2f, -1f, 1f)
        } else {
            //竖屏
            Matrix.orthoM(mMatrix, 0, -.5f, .5f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //使用程序片段
        GLES30.glUseProgram(mProgram)
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, mTexVertexBuffer)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        //绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        // 绘制
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            VERTEX_INDEX.size,
            GLES20.GL_UNSIGNED_SHORT,
            mVertexIndexBuffer
        )
    }
}