package com.newland.opengl.utils

import android.content.Context
import com.newland.common.OpenBaseApplication
import java.io.BufferedReader
import java.io.InputStreamReader

object ResourceUtils {
    fun readResource(resourceId: Int): String =
        readResource(OpenBaseApplication.application, resourceId)

    fun readResource(context: Context, resourceId: Int): String {
        val builder = StringBuilder()
        try {
            val inputStream = context.resources
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