package ru.is2si.sisi.presentation.files

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import org.threeten.bp.LocalDateTime
import ru.is2si.sisi.BuildConfig
import ru.is2si.sisi.base.extension.getDateTimeOfPattern
import java.io.BufferedOutputStream
import java.io.File
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

    @Throws(IOException::class)
    fun saveImage(ctx: Context, doAfterSave: (photoPath: String, photoURI: Uri) -> Unit) {
        val photoFile = createPhoto(
                ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        ?: throw IOException()
        )

        photoFile.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                    ctx,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    it
            )
            doAfterSave(photoFile.absolutePath, photoURI)
        }
    }

    @Throws(IOException::class)
    private fun createPhoto(filesDir: File): File = File.createTempFile(
            LocalDateTime.now().getDateTimeOfPattern("dd_LL_YYYY_HH_mm_SS"),
            ".jpg",
            filesDir
    )
}
