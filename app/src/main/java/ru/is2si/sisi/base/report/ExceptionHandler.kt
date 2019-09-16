package ru.is2si.sisi.base.report

import android.os.Build
import android.os.Environment
import android.util.Log
import org.threeten.bp.LocalDateTime
import ru.is2si.sisi.BuildConfig
import ru.is2si.sisi.base.extension.getDateTimeOfPattern
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

internal class ExceptionHandler private constructor(
        chained: Boolean
) : Thread.UncaughtExceptionHandler {

    private val formatter = LocalDateTime.now().getDateTimeOfPattern("dd_LL_YYYY_HH_mm_ss")
    private val fileFormatter = LocalDateTime.now().getDateTimeOfPattern("dd-LL-YYYY")
    private var versionName = "0"
    private var versionCode = 0
    private val stacktraceDir: String
    private val previousHandler: Thread.UncaughtExceptionHandler?

    init {
        versionName = BuildConfig.VERSION_NAME
        versionCode = BuildConfig.VERSION_CODE
        previousHandler = if (chained)
            Thread.getDefaultUncaughtExceptionHandler()
        else
            null
        stacktraceDir = String.format("/Android/data/%s/files/", BuildConfig.APPLICATION_ID)
    }

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val state = Environment.getExternalStorageState()
        val dumpDate = Date(System.currentTimeMillis())
        if (Environment.MEDIA_MOUNTED == state) {

            val reportBuilder = StringBuilder()
            reportBuilder
                    .append("\n\n\n")
                    .append(formatter.format(dumpDate)).append("\n")
                    .append(String.format("Version: %s (%d)\n", versionName, versionCode))
                    .append("Device: ${Build.BRAND} ${Build.MODEL}\n")
                    .append("Android ${Build.VERSION.SDK_INT}(API)\n")
                    .append(thread.toString()).append("\n")
            processThrowable(exception, reportBuilder)

            val sd = Environment.getExternalStorageDirectory()
            val stacktraceFile = File(
                    sd.path + stacktraceDir,
                    String.format(
                            "stacktrace-%s.txt",
                            fileFormatter.format(dumpDate)
                    )
            )
            val dumpDir = stacktraceFile.parentFile
            val dirReady = dumpDir.isDirectory || dumpDir.mkdirs()
            if (dirReady) {
                var writer: FileWriter? = null
                try {
                    writer = FileWriter(stacktraceFile, true)
                    writer.write(reportBuilder.toString())
                } catch (err: IOException) {
                    if (BuildConfig.DEBUG) Log.e("_debug", "ExceptionHandler error", err)
                }
                writer?.close()

            }
        }
        previousHandler?.uncaughtException(thread, exception)
    }

    private fun processThrowable(exception: Throwable?, builder: StringBuilder) {
        if (exception == null)
            return
        val stackTraceElements = exception.stackTrace
        builder
                .append("Exception: ").append(exception.javaClass.name).append("\n")
                .append("Message: ").append(exception.message).append("\nStacktrace:\n")
        for (element in stackTraceElements) {
            builder.append("\t").append(element.toString()).append("\n")
        }
        processThrowable(exception.cause, builder)
    }

    companion object {
        fun reportLogAndHandler(): ExceptionHandler = ExceptionHandler(true)

        fun reportOnlyHandler(): ExceptionHandler = ExceptionHandler(false)
    }
}