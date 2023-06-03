package com.baima.baimapuzzle

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_puzzle_help.*

class PuzzleHelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.help)
        setContentView(R.layout.activity_puzzle_help)

        val list = mutableListOf<String>()
        val text = assets.open("readme.md").bufferedReader().use { it.readText() }
        for (line in text.split("\n")) {
            if (line != null && line != "" && line != "\n" && line != "\r") {
                list.add(line)
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        lv.adapter = adapter
    }
}
