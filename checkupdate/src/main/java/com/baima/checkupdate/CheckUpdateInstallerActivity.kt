package com.baima.checkupdate

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInstaller
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream


//可以安装应用存储目录下的apk文件
class CheckUpdateInstallerActivity : AppCompatActivity() {

    //intent-filter 的 action
    private val ACTION_INSTALL = "com.baima.checkupdate.INSTALL_APK"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.install_application)
        setContentView(R.layout.activity_check_update_installer)

        val apkPath = intent.getStringExtra(EXTRA_APK_PATH)
        if (apkPath != null) {
            install(apkPath)
        }
    }

    /**
     * 安装
     */
    private fun install(apkPath: String) {
        packageManager.packageInstaller.apply {
            //生成参数
            val params =
                PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            //创建ID
            val id = createSession(params)
            //打开Session
            val session = openSession(id)
            //写入文件
            writeApk2Session(session, apkPath)
            //新建IntentSender
            val intent = createIntentSender()
            //提交，进行安装
            session.commit(intent)
        }
    }

    /**
     * 新建一个IntentSender用于接收结果
     * 该例子通过当前页面接收
     */
    private fun createIntentSender(): IntentSender {
        val intent = Intent(this, CheckUpdateInstallerActivity::class.java).apply {
            action = ACTION_INSTALL
        }
        val pending = PendingIntent.getActivity(this, 0, intent, 0)
        return pending.intentSender
    }

    //写入Apk到Session输出流，
    private fun writeApk2Session(session: PackageInstaller.Session, apkPath: String) {
        val fis = FileInputStream(apkPath)
        fis.use { input ->
            session.openWrite("apk", 0, -1).use { output ->
                output.write(input.readBytes())
            }
        }
        fis.close()
    }

    /**
     * 接收安装结果
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent ?: return
        if (intent.action != ACTION_INSTALL) {
            return
        }
        intent.extras?.apply {
            when (this.getInt(PackageInstaller.EXTRA_STATUS)) {
                PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                    //提示用户进行安装
                    startActivity(this.get(Intent.EXTRA_INTENT) as Intent)
                }
                PackageInstaller.STATUS_SUCCESS -> {
                    //安装成功
                    finish()
                }
                else -> {
                    finish()
                    //失败信息
                    val msg = this.getString(PackageInstaller.EXTRA_STATUS_MESSAGE)
                }
            }
        }
    }

    companion object {
        val EXTRA_APK_PATH = "apk_path"
    }
}