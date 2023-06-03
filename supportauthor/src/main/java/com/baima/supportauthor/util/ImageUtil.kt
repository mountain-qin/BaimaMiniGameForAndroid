package com.baima.supportauthor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File


class ImageUtil private constructor() {
    companion object {

        fun saveImageBitmap(
            context: Context,
            path: String,
            bitmap: Bitmap,
            format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
        ): Boolean {
            var saved = false
            try {
                val file = File(path)
                val parentFile = file.parentFile
                if (!(parentFile?.exists() ?: true)) {
                    parentFile?.mkdirs()
                }

                val outputStream = file.outputStream()
                bitmap.compress(format, 80, outputStream)
                outputStream.flush()
                outputStream.close()

                //发广播告诉相册有图片需要更新，这样可以在图册下看到保存的图片了
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri: Uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)

                saved = true
            } catch (e: Exception) {

            }
            return saved
        }


        fun saveImageResource(
            context: Context,
            path: String,
            resId: Int,
            format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
        ): Boolean {
            val bitmap = BitmapFactory.decodeResource(context.resources, resId)
            return saveImageBitmap(context, path, bitmap, format)
        }
    }
}