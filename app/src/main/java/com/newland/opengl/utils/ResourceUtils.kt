package com.newland.opengl.utils

import android.content.Context
import android.provider.OpenableColumns
import com.newland.opengl.app.OpenGlApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

object ResourceUtils {
    fun readResource(resourceId: Int): String =
        readResource(OpenGlApplication.application, resourceId)

    fun readResource(context: Context, resourceId: Int): String {
        val builder = StringBuilder()
        try {
            val inputStream = context.getResources()
                .openRawResource(resourceId)
            val streamReader = InputStreamReader(inputStream)

            val bufferedReader = BufferedReader(streamReader)
            var textLine: String?
            while (bufferedReader.readLine().also { textLine = it } != null) {
                builder.append(textLine)
                builder.append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return builder.toString()
    }
}