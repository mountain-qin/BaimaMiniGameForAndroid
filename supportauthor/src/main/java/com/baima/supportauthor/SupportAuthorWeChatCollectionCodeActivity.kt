package com.baima.supportauthor

import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * 微信收款码界面
 */
class SupportAuthorWeChatCollectionCodeActivity : SupportAuthorCollectionCodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getSupportAuthorInstance(): SupportAuthor? {
        return WeChatSupportAuthor.getInstance()
    }
}
