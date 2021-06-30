package com.newland.opengl

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.newland.demo1.Demo1MainActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<AppCompatButton>(R.id.btn_simple).setOnClickListener(this)
        findViewById<AppCompatButton>(R.id.btn_basic).setOnClickListener(this)
        findViewById<AppCompatButton>(R.id.btn_demo1).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_simple -> startActivity(Intent(this, CsdnActivity::class.java))
            R.id.btn_basic -> startActivity(Intent(this, OpenGLActivity::class.java))
            R.id.btn_demo1 -> startActivity(Intent(this, Demo1MainActivity::class.java))
        }
    }

}