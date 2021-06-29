package com.newland.opengl

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<AppCompatButton>(R.id.btn_simple).setOnClickListener(this)
        findViewById<AppCompatButton>(R.id.btn_basic).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_simple -> startActivity(Intent(this, CsdnActivity::class.java))
            R.id.btn_basic -> startActivity(Intent(this, OpenGLActivity::class.java))
        }
    }

}