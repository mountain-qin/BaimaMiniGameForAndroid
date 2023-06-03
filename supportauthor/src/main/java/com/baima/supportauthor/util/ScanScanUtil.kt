package com.baima.supportauthor

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri


/**
 * 扫一扫的工具类；
 *打开微信、支付宝的扫一扫
 */
class ScanScanUtil private constructor() {
    companion object {
        /**
         * 打开微信的扫一扫
         */
        fun openWeChatScanScan(context: Context) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


        /**
         * 打开支付宝扫一扫
         *中国是东8区。，支付宝扫一扫打开相册 ，图片时间晚8个小时，
         */
        fun openAlipayScanScan(context: Context) {
            val uri =
                Uri.parse("alipayqr://platformapi/startapp?saId=10000007")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }
}