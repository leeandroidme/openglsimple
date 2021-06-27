package com.newland.opengl.extend

import android.app.Activity
import android.content.Intent

fun Activity.startActivity(clazz: Class<out Activity>) {
    startActivity(Intent(this, clazz))
}