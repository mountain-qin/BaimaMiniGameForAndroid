package com.baima.baimaminigame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.baima.baimapuzzle.PuzzleActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var games:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context=this
        init()
    }

    private fun init() {
games= arrayOf(getString(R.string.baima_puzzle))
        val gameAdapter=ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, games)
        lv_game.adapter=gameAdapter
        lv_game.setOnItemClickListener { parent, view, position, id ->
            when(position){
                0->startActivity(Intent(context, PuzzleActivity::class.java))
            }
        }
    }
}
