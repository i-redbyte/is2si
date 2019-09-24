package ru.is2si.sisi.presentation.files

import android.content.ContentResolver
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import ru.is2si.sisi.BuildConfig
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException

class FilesHandler(private val contentResolver: ContentResolver) {

    fun saveTrackFileToTmpDirectory(
            uri: Uri,
            doAfterSave: (filePath: String) -> Unit
    ) {
        val fileName = uri.path?.split("/".toRegex())?.last() ?: ""
        val mime = MimeTypeMap.getSingleton()
        val extension = mime.getExtensionFromMimeType(contentResolver.getType(uri))
        val findDot = fileName.indexOf(".")
        val destinationFilename = if (extension.isNullOrEmpty() || findDot > -1)
            Environment.getExternalStorageDirectory().path +
                    "/Android/data/${BuildConfig.APPLICATION_ID}/files/$fileName"
        else
            Environment.getExternalStorageDirectory().path +
                    "/Android/data/${BuildConfig.APPLICATION_ID}/files/$fileName.$extension"
        val bis = contentResolver.openInputStream(uri)
        var bos: BufferedOutputStream? = null
        try {
            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
            val buf = ByteArray(1024)
            bis?.read(buf)
            do {
                bos.write(buf)
            } while (bis?.read(buf) != -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        doAfterSave(destinationFilename)
    }
}
