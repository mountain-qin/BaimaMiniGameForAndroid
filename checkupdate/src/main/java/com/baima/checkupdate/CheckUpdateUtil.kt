package com.baima.checkupdate

import android.content.Context
import android.os.Environment
import android.util.Log
import com.baima.checkupdate.util.InstallUtil
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.File

/**
 * 用github release作检查 更新功能
 */
class CheckUpdateUtil private constructor() {
    companion object {
        /**
         * 检查有没有新版本
         */
        fun checkNewVersion(
            context: Context,
            latestUrl: String,
            onFindedNewVersion: (version: String) -> Unit
        ) {
            OkGo.get<String>(latestUrl)
                .execute(object : StringCallback() {
                    override fun onSuccess(response: Response<String>?) {
                        if (response == null) {
                            return
                        }

                        try {
                            val jsonObject = JSONObject(response?.body())
                            var tagName = jsonObject.getString("tag_name")
                            tagName = tagName.toLowerCase()
                            var versionName =
                                context.packageManager.getPackageInfo(context.packageName, 0)
                                    .versionName
                            versionName = "v${versionName}".toLowerCase()

                            if (versionName != tagName) {
                                onFindedNewVersion(tagName)
                            }
                        } catch (e: ExceptionInInitializerError) {

                        }

                    }
                })
        }

        //下载失败后重试的次数，github很难下载
        private var downloadErrorCount = 0


        /**
         * 检查更新
         * 如果有新版本弹出下载对话框
         *@param latestUrl 最新版本的发布地址
         * @param apkName 要下载的apk名字
         */
        fun checkUpdate(
            context: Context,
            latestUrl: String,
            apkName: String,
            onDownloadStart:()->Unit={}
        ) {
            downloadErrorCount = 10
            OkGo.get<String>(latestUrl)
                .execute(object : StringCallback() {
                    override fun onSuccess(response: Response<String>?) {
                        if (response == null) {
                            return
                        }

                        try {
                            val jsonObject = JSONObject(response.body())
                            //如：v1.0
                            val tagName = jsonObject.getString("tag_name").toLowerCase()
                            val versionName =
                                "v${context.packageManager.getPackageInfo(
                                    context.packageName,
                                    0
                                ).versionName}".toLowerCase()
                            if (tagName == versionName) {
//已经是最新版本
                                context.alert(context.getString(R.string.latest_version_now), "") {
                                    positiveButton(R.string.ok) {}
                                }
                                    .show()
                                return
                            }

                            val assets = jsonObject.getJSONArray("assets")
                            for (i in 0..assets.length() - 1) {
                                val asset = assets.getJSONObject(i)
                                val name = asset.getString("name")
                                val downloadUrl = asset.getString("browser_download_url")
                                if (name == apkName) {
                                    context.alert(
                                        "${jsonObject.getString("body")}",
                                        "${context.getString(R.string.finded_new_version)}: $tagName"
                                    ) {
                                        //下载按钮
                                        positiveButton(R.string.download) {
                                            downloadAsset(context, downloadUrl)
                                            onDownloadStart()
                                        }
                                        negativeButton(R.string.cancel) {}
                                    }
                                        .show()
                                    return
                                }
                            }
                        } catch (e: Exception) {
                            context.toast(R.string.occurred_error)
                        }
                    }


                    override fun onError(response: Response<String>?) {
                        super.onError(response)
                        context.toast(R.string.occurred_error)
                    }
                })
        }


        /**
         * 下载资源
         */
        private fun downloadAsset(context: Context, downloadUrl: String) {
            val tag = "downloadAsset"
            //下载到应用的存储目录下的download目录
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
            val fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1)

            OkGo.get<File>(downloadUrl)
                .tag(tag)
                .execute(object : FileCallback(dir, fileName) {
                    override fun onSuccess(response: Response<File>?) {
                        if (response == null) {
                            return
                        }

                        InstallUtil.install(context, response?.body().absolutePath)
                        OkGo.getInstance().cancelTag(tag)
                    }

                    override fun onError(response: Response<File>?) {
                        super.onError(response)
                        Log.i("baima", "\nerror: ${response?.exception}")

                        //下载失败重新下载
                        if (downloadErrorCount > 0) {
                            downloadAsset(context, downloadUrl)
                            downloadErrorCount -= 1
                        }
                    }
                })
        }
    }
}