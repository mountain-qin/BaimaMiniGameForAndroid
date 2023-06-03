package com.baima.supportauthor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * 支付宝收款码界面
 */
class SupportAuthorAlipayCollectionCodeActivity : SupportAuthorCollectionCodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun getSupportAuthorInstance(): SupportAuthor? {
        return AlipaySupportAuthor.getInstance()
    }
}
