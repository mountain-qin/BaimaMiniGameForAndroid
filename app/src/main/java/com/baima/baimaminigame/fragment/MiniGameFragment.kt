package com.baima.baimaminigame.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.baima.baimaminigame.MainActivity
import com.baima.baimaminigame.R
import com.baima.baimapuzzle.PuzzleActivity
import com.baima.checkupdate.CheckUpdateUtil
import kotlinx.android.synthetic.main.fragment_mini_game.view.*

class MiniGameFragment : Fragment() {

    private lateinit var games: Array<String>
    private lateinit var tv_newVersion: TextView
    private lateinit var lv_game: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mini_game, container, false)
        tv_newVersion = view.tv_new_version
        lv_game = view.lv_game
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    fun initData() {
        if (activity != null) {
            CheckUpdateUtil.checkNewVersion(
                activity!!,
                (activity!! as MainActivity).latestUrl,
                { version ->
                    tv_newVersion.text = "${getString(R.string.finded_new_version)}: $version"
                    tv_newVersion.visibility = View.VISIBLE
                })

            tv_newVersion.setOnClickListener {
                CheckUpdateUtil.checkUpdate(
                    context!!,
                    (activity!! as MainActivity).latestUrl,
                    (activity!! as MainActivity).apkName,
                    {tv_newVersion.visibility = View.GONE}
                )
            }
        }

        games = arrayOf(getString(R.string.baima_puzzle))
        val adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_list_item_1, games)
        lv_game.adapter = adapter

        //不起作用
        lv_game.requestFocus()
        lv_game.requestFocusFromTouch()

        lv_game.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> startActivity(Intent(activity, PuzzleActivity::class.java))
            }
        }
    }


}