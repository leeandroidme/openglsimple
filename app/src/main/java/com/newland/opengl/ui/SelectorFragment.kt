package com.newland.opengl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.newland.opengl.R
import com.newland.opengl.common.OpenGLConstant

class SelectorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = RecyclerView(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view as RecyclerView
        view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val data = mutableListOf("纯色背景","繪製點、線、三角形")
        val adapter = OpenGlAdapter(data)
        view.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                0 -> Navigation.findNavController(requireActivity(), R.id.fragmentContainer)
                    .navigate(SelectorFragmentDirections.actionRenderer(OpenGLConstant.BACKGROUND))
                1 -> Navigation.findNavController(requireActivity(), R.id.fragmentContainer)
                    .navigate(SelectorFragmentDirections.actionRenderer(2))
            }
        }
    }

    class OpenGlAdapter(data: MutableList<String>?) :
        BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_list_item_1, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(android.R.id.text1, item)
        }
    }
}