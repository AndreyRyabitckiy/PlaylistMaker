package com.example.playlistmaker.playlist_create.data

import android.content.Context
import android.provider.OpenableColumns
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SaveImageToMemory(private val context: Context) {
    suspend fun saveImageToFile(uri: String): String {
        val name = context.contentResolver.query(uri.toUri(), null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
        val file = File(context.filesDir, name)
        if (!file.exists()) {
            withContext(Dispatchers.IO) {
                file.createNewFile()
            }
        }
        val inputStream = context.contentResolver.openInputStream(uri.toUri())
        val outputStream = file.outputStream()

        outputStream.write(inputStream?.readBytes())
        return file.absolutePath
    }
}