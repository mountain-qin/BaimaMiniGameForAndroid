package com.baima.baimapuzzle

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_puzzle_row_col_setting.*
import org.jetbrains.anko.toast

class PuzzleRowColSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_row_col_setting)

        bt_cancel.setOnClickListener {
            finish()
        }

        bt_ok.setOnClickListener {
            val rowStr = et_row.text.toString()
            val colStr = et_col.text.toString()
            if (rowStr == "") {
                toast(R.string.row_count_is_not_empty)
                return@setOnClickListener
            }

            if (colStr == "") {
                toast(R.string.column_count_is_not_empty)
                return@setOnClickListener
            }

            intent.putExtra(PuzzleActivity.EXTRA_ROW, rowStr.toInt())
            intent.putExtra(PuzzleActivity.EXTRA_COL, colStr.toInt())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
