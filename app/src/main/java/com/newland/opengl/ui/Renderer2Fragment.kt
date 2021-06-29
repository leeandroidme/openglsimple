package com.newland.opengl.ui

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.newland.opengl.common.IRendererLife
import com.newland.opengl.renderer2.*
import kotlinx.coroutines.GlobalScope

class Renderer2Fragment : Fragment() {
    private var renderer: GLSurfaceView.Renderer? = null
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
            1 -> TriangleRenderer()
            else -> null
        }?.also { it ->
            view.setRenderer(it)
            view.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        renderer?.apply {
            if (this is IRendererLife) {
                (this as IRendererLife).shudown()
            }
        }
    }
}