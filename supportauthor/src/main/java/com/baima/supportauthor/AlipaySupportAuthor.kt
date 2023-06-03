package com.baima.supportauthor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

/**
 * 支付宝支持作者
 */
class AlipaySupportAuthor(context: Context, collectionCode: Bitmap, fileDir: String = "") :
    SupportAuthor(context, collectionCode, fileDir = fileDir) {

    override val collectionCodeName: String
        get() = context.getString(R.string.alipay_collection_code)
    override val fileName: String
        get() = "${context.getString(R.string.alipay_collection_code_of_the_author)}.jpg"
    override val format: Bitmap.CompressFormat
        get() = Bitmap.CompressFormat.JPEG

    override fun viewAuthorCollectionCode() {
        val intent = Intent(context, SupportAuthorAlipayCollectionCodeActivity::class.java)
        context.startActivity(intent)
    }

    protected override fun openScanScan() {
        ScanScanUtil.openAlipayScanScan(context)
    }


    companion object {
        private var instance: AlipaySupportAuthor? = null


        fun getInstance(
            context: Context,
            collectionCode: Bitmap,
            fileDir: String = ""
        ): AlipaySupportAuthor {
            if ((instance == null)) {
                instance = AlipaySupportAuthor(context, collectionCode, fileDir = fileDir)
            }
            return instance!!
        }


        fun getInstance(): AlipaySupportAuthor? {
            return instance
        }
    }
}