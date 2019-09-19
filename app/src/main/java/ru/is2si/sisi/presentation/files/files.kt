package ru.is2si.sisi.presentation.files

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import ru.is2si.sisi.BuildConfig


fun getPath(context: Context, uri: Uri): String? {

    if (isExternalStorageDocument(uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]

        if ("primary".equals(type, ignoreCase = true)) {
            return "${Environment.getExternalStorageDirectory()}/${split[1]}"
        }

    } else if (isDownloadsDocument(uri)) {
        val id = DocumentsContract.getDocumentId(uri)
        return getDataColumn(context, uri, MediaStore.Images.Media._ID + "=?", listOf())
    } else if (isMediaDocument(uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]
        val contentUri: Uri = when (type) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val selection = "_id=?"
        val selectionArgs = arrayListOf(split[1])

        return getDataColumn(context, contentUri, selection, selectionArgs)
    }
    return null
}

@SuppressLint("Recycle")
fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: List<String>
): String? {
    var filePath = ""
    try {
        val wholeID = DocumentsContract.getDocumentId(uri)
        val column = arrayOf(MediaStore.Images.Media.DATA)
        val args = selectionArgs.toMutableList()
        args.add(wholeID)
        context.contentResolver.query(
                uri,
                column,
                null,
                null,
                null
        )?.let {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            filePath = it.getString(columnIndex)
            it.close()

        }
    } catch (err: Exception) {
        if (BuildConfig.DEBUG) Log.e("_debug", "err", err)
    }
    return filePath
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}