package com.baima.supportauthor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

/**
 * 微信支持作者
 */
class WeChatSupportAuthor(context: Context, collectionCode: Bitmap, fileDir: String = "") :
    SupportAuthor(context, collectionCode, fileDir = fileDir) {
    override val collectionCodeName: String
        get() = context.getString(R.string.we_chat_collection_code)
    override val fileName: String
        get() = "${context.getString(R.string.we_chat_collection_code_of_the_author)}.png"
    override val format: Bitmap.CompressFormat
        get() = Bitmap.CompressFormat.PNG

    override fun viewAuthorCollectionCode() {
        val intent = Intent(context, SupportAuthorWeChatCollectionCodeActivity::class.java)
        context.startActivity(intent)
    }

    protected override fun openScanScan() {
        ScanScanUtil.openWeChatScanScan(context)
    }

    companion object {
        private var instance: WeChatSupportAuthor? = null


        fun getInstance(
            context: Context,
            collectionCode: Bitmap,
            fileDir: String = ""
        ): WeChatSupportAuthor {
            if (instance == null) {
                instance = WeChatSupportAuthor(context, collectionCode, fileDir = fileDir)
            }
            return instance!!
        }


        fun getInstance(): WeChatSupportAuthor? {
            return instance
        }
    }
}