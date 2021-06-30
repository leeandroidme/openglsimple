package com.newland.opengl.ui

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.newland.opengl.render.*

class RendererFragment : Fragment() {
    private val args: RendererFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = GLSurfaceView(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view as GLSurfaceView
        view.setEGLContextClientVersion(3)
        when (args.flag) {
            1 -> BackgroundRender()
            2 -> ColoursShapeRender()
            3 -> SimpleShapeRender()
            4 -> SimpleShapeRender()
            5 -> RectangleRenderer()
            6 -> ColorCubeRenderer()
            7 -> EnableVertexRenderer()
            8 -> IndicesCubeRenderer()
            9 -> LineCubeRenderer()
            10 -> MapBufferRenderer()
            11 -> RectangleWRenderer()
            12 -> UniformRenderer()
            13 -> VertexArrayRenderer()
            14 -> VertexPointerRenderer()
            15 -> VertexBufferRenderer()
            else -> null
        }?.also { it ->
            view.setRenderer(it)
        }
    }
}