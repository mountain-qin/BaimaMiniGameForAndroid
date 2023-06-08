package com.baima.checkupdate.util

import android.content.Context
import android.content.Intent
import android.os.Build
import com.baima.checkupdate.CheckUpdateInstallerActivity

class InstallUtil private constructor() {
    companion object {
        fun install(context: Context, apkPath: String) {
            //5.0以上
            //11可用
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val intent = Intent(context, CheckUpdateInstallerActivity::class.java)
                intent.putExtra(CheckUpdateInstallerActivity.EXTRA_APK_PATH, apkPath)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                //5.0以下

            }
        }
    }
}