package com.baima.baimaminigame

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.baima.baimaminigame.fragment.MiniGameFragment
import com.baima.baimaminigame.fragment.MoreFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    val latestUrl =
        "https://api.github.com/repos/mountain-qin/BaimaMiniGameForAndroid/releases/latest"
    val apkName = "BaimaMiniGame.apk"

    private lateinit var context: Context
    private lateinit var games: Array<String>
    private lateinit var fragmentList: List<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        requestPermissions()
        initViews()
    }


    private fun requestPermissions() {
        val permissionList = mutableListOf<String>()
        val permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    //不弹出 请求权限的对话框
//            Manifest.permission.REQUEST_INSTALL_PACKAGES
        )

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.add(permission)
            }
        }

        if (permissionList.size > 0) {
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), 1)
        } else {

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults != null) {
                    for (grantResult in grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            toast(R.string.denying_permission_message)
                            return
                        }
                    }
                    //
                }
            }
        }
    }

    private fun initViews() {
        fragmentList = listOf<Fragment>(MiniGameFragment(), MoreFragment())
        val titleResIdList = listOf(R.string.mini_game, R.string.more)

        val mainFragmentPagerAdapter =
            MainFragmentPagerAdapter(context, supportFragmentManager, fragmentList, titleResIdList)

        view_pager.adapter = mainFragmentPagerAdapter
//view_pager.adapter 之后
        tl_bottom_navigation.setupWithViewPager(view_pager)

        //ViewPager设置页面改变监听器
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

            }
        })
    }
}
