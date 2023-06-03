package com.baima.supportauthor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_support_author_collection_code.*
import org.jetbrains.anko.toast

/**
 * 收款码界面
 */
abstract class SupportAuthorCollectionCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_author_collection_code)

        val context = this
        val supportAuthor = getSupportAuthorInstance()
        title = supportAuthor?.collectionCodeName
        iv_collection_code.setImageBitmap(supportAuthor?.collectionCode)

        iv_collection_code.setOnClickListener {
            Thread {
                supportAuthor?.openScanScanCollectionCode()
            }.start()
        }

        //保存到相册
        bt_save_to_album.setOnClickListener {
            Thread {
                if (supportAuthor?.saveToAlbum() ?: false) {
                    runOnUiThread { toast(R.string.image_saved_successfully) }
                } else {
                    runOnUiThread { toast(R.string.image_save_failed) }
                }
            }.start()
        }
    }

    /**
     * 获取支持作者的实例
     */
    abstract fun getSupportAuthorInstance(): SupportAuthor?


}
