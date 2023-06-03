package com.baima.supportauthor

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import java.io.File

/**
 * 支持作者
 */
abstract class SupportAuthor
/**
 *@param collectionCode 收款码
 * @param fileDir 打开    扫一扫的时候会保存收款码到相册，fileDir是子目录，默认是应用名称
 */
@JvmOverloads constructor(
    val context: Context,
    val collectionCode: Bitmap,
    var fileDir: String = ""
) {
    /**
     * 收款码名字
     */
    abstract val collectionCodeName: String
    /**
     * 收款码保存到相册的文件名
     */
    abstract val fileName: String
    /**
     * 图片压缩格式
     */
    abstract val format: Bitmap.CompressFormat

    /**
     * 查看作者收款码
     */
    abstract fun viewAuthorCollectionCode()

    /**
     * 打开扫一扫
     */
    protected abstract fun openScanScan()


    init {
        if (fileDir == "") {
            //app_name
            fileDir = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            ).loadLabel(context.packageManager).toString()
        }
    }

    /**
     * 收款码保存到相册，就是DCIM目录
     *dir 子目录，默认是应用名称
     * 建议在子线程中执行
     */
    fun saveToAlbum(dir: String = fileDir): Boolean {
        val path = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "${dir}/${fileName}"
        )
            .absolutePath

        return ImageUtil.saveImageBitmap(context, path, collectionCode, format)
    }


    /**
     * 打开扫一扫，扫收款码
     * 不能把图片传给扫一扫的软件，
     * 这个方法先保存收款码到相册，然后打开扫一扫，
     * 再由用户点击扫一扫里的相册，第1 张图片就是收款码了。
     * 建议在子线程执行
     */
    public fun openScanScanCollectionCode() {
        if (saveToAlbum()) {
            this.openScanScan()
        }
    }

}