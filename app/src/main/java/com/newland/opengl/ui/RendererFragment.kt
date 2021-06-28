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
            3 -> BackgroundNativeRenderer()
            4 -> SimpleShapeRender()
            5 -> RectangleRenderer()
            6 -> ColorCubeRenderer()
            7 -> EnableVertexRenderer()
            else -> null
        }?.also { it ->
            view.setRenderer(it)
        }
    }
}