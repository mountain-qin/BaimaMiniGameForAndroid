package com.baima.baimaminigame.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.baima.baimaminigame.MainActivity
import com.baima.baimaminigame.R
import com.baima.checkupdate.CheckUpdateUtil
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_more, null)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        if (activity != null) {
            val versionName =
                activity!!.packageManager.getPackageInfo(activity!!.packageName, 0).versionName
            tv_version.text = "${getString(R.string.version)}: $versionName"

            tv_check_update.setOnClickListener {
                CheckUpdateUtil.checkUpdate(
                    activity!!,
                    (activity!! as MainActivity).latestUrl,
                    (activity!! as MainActivity).apkName
                )
            }
        }

    }

}