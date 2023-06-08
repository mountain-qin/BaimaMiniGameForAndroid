package com.baima.baimapuzzle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.get
import com.baima.baimapuzzle.util.ScreenUtil
import com.baima.bgmsound.BgmMediaPlayer
import com.baima.bgmsound.BgmSoundSettingActivity
import com.baima.bgmsound.SoundPlayer
import com.baima.supportauthor.AlipaySupportAuthor
import com.baima.supportauthor.WeChatSupportAuthor
import kotlinx.android.synthetic.main.activity_puzzle.*
import org.jetbrains.anko.toast
import kotlin.math.abs

class PuzzleActivity : AppCompatActivity() {
    private val fileDir="BaimaMiniGame"

    private var startX: Float? = null
    private var startY: Float? = null
    private var endX: Float? = null
    private var endY: Float? = null

    private var isLongPress = false

    private lateinit var context: Context
    private lateinit var puzzle: Puzzle
    private lateinit var layout_blocks: RelativeLayout
    private lateinit var bgmMediaPlayer: BgmMediaPlayer
    private lateinit var soundPlayer: SoundPlayer
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.baima_puzzle)
        setContentView(R.layout.activity_puzzle)

        context = this
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE)

//        showFirstHelp()

        val row = sharedPreferences.getInt(SHARED_PREFERENCE_ROW, Puzzle.DEFAULT_ROW)
        val col = sharedPreferences.getInt(SHARED_PREFERENCE_COL, Puzzle.DEFAULT_COL)
        val emptyGridTitle = getString(R.string.empty)
        puzzle = Puzzle(row, col, emptyGridTitle)

        addBlocks()
        initBgmSound()
    }

    private fun showFirstHelp() {
        val isFirst = sharedPreferences.getBoolean(SHARED_PREFERENCE_IS_FIRST, true)
        if (isFirst) {
            startActivity(Intent(context, PuzzleHelpActivity::class.java))

            sharedPreferences.edit()
                .putBoolean(SHARED_PREFERENCE_IS_FIRST, false)
                .apply()
        }
    }

    private fun initBgmSound() {
        bgmMediaPlayer = BgmMediaPlayer.getInstance(context, "bgm.mp3")
        val soundResIds = arrayOf(
            R.raw.boundary_puzzle,
            R.raw.congratulation_puzzle,
            R.raw.move_puzzle,
            R.raw.unmove_puzzle
        )
        soundPlayer = SoundPlayer.getInstance(context, soundResIds)

        bgmMediaPlayer.play()
    }


    override fun onDestroy() {
        super.onDestroy()
        bgmMediaPlayer.release()
        soundPlayer.release()
    }


    private val onBlockClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            val map = v?.tag as MutableMap<Int, Int>
            val row = map.get(KEY_ROW)
            val col = map.get(KEY_COL)

            puzzle.setFocusBlockRow(row!!)
            puzzle.setFocusBlockCol(col!!)

            var moved = false
            moved = (moved or puzzle.moveUp()
                    or puzzle.moveDown()
                    or puzzle.moveLeft()
                    or puzzle.moveRight()
                    )

            showMoveResult(moved)
        }
    }

    /**
     * 添加方块
     */
    private fun addBlocks() {
        layout_main_activity.removeAllViews()

        val blocks = puzzle.getBlocks()

        val screenWidth = ScreenUtil.getScreenWidth(this)
        val screenHeight = ScreenUtil.getScreenHeight(this)

        var blockWidth = screenWidth / puzzle.col
        var blockHeight = screenHeight / (puzzle.row + 1)
        //让方块是正方，
        //方块的宽高 哪个小就把宽高都设置为小的
        blockWidth = if (blockWidth <= blockHeight) blockWidth else blockHeight
        blockHeight = blockWidth

        //方块的父布局
        layout_blocks = RelativeLayout(context)
//        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        val layoutParams =
            RelativeLayout.LayoutParams(blockWidth * puzzle.col, blockHeight * (puzzle.row + 1))
        //剧中
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        layoutParams.setMargins(2, 2, 2, 2)
        layout_main_activity.addView(layout_blocks, layoutParams)

        val myOnTouchListener = MyOnTouchListener()

        //添加方块到父布局
        for (row in 0..puzzle.row) {
            for (col in 0..puzzle.col - 1) {
                val textView = TextView(context)
                if (row == 0 && col != 0) {
                    textView.setText(blocks[row][col])
                    textView.visibility = View.GONE
                }

                //1,1行 1列
                textView.setText(
                    "${blocks[row][col]},\n$row${getString(R.string.row)} ${col + 1}${getString(
                        R.string.column
                    )}"
                )

                textView.x = blockWidth * col.toFloat()
                textView.y = blockHeight * row.toFloat()

                val layoutParams1 = RelativeLayout.LayoutParams(blockWidth, blockHeight)
                layoutParams1.setMargins(2, 2, 2, 2)

                textView.tag = mapOf(KEY_ROW to row, KEY_COL to col)

                layout_blocks.addView(textView, layoutParams1)

                textView.setOnTouchListener(myOnTouchListener)
                textView.setOnClickListener(onBlockClickListener)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_puzzle_activity, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //调整顺序
            R.id.item_adjust_order -> {
                puzzle.adjustOrder()
                refreshPuzzleInformation()
                toast(R.string.order_adjusted)
            }
            //打乱顺序
            R.id.item_disrupt_order -> {
                puzzle.disruptOrder()
                refreshPuzzleInformation()
                toast(R.string.order_disrupted)
            }
            //背景音乐音效设置
            R.id.item_bgm_sound_setting -> startActivity(
                Intent(
                    context,
                    BgmSoundSettingActivity::class.java
                )
            )
            //设置行列数
            R.id.item_row_col_setting -> startActivityForResult(
                Intent(
                    context,
                    PuzzleRowColSettingActivity::class.java
                ), REQUEST_SET_ROW_COL
            )
            //帮助
            R.id.item_help -> startActivity(Intent(context, PuzzleHelpActivity::class.java))
//查看作者微信收款码
            R.id.item_view_author_we_chat_collection_code -> {
                WeChatSupportAuthor.getInstance(
                    context,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.we_chat_collection_code_puzzle
                    ),
                    fileDir
                )
                    .viewAuthorCollectionCode()
            }
            //微信扫一扫支持作者
            R.id.item_we_chat_scan_scan_support_author -> {
                WeChatSupportAuthor.getInstance(
                    context,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.we_chat_collection_code_puzzle
                    ),
                    fileDir
                )
                    .openScanScanCollectionCode()
            }
//查看作者支付宝收款码
            R.id.item_view_author_alipay_collection_code -> {
                AlipaySupportAuthor.getInstance(
                    context,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.alipay_collection_code_puzzle
                    ),
                    fileDir
                )
                    .viewAuthorCollectionCode()
            }
//支付宝扫一扫支持作者
            R.id.item_alipay_scan_scan_support_author -> {
                AlipaySupportAuthor.getInstance(
                    context,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.alipay_collection_code_puzzle
                    ),
                    fileDir
                )
                    .openScanScanCollectionCode()
            }
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //选择行列数
            REQUEST_SET_ROW_COL -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val row = data.getIntExtra(EXTRA_ROW, puzzle.row)
                    val col = data.getIntExtra(EXTRA_COL, puzzle.col)
                    puzzle = Puzzle(row, col, puzzle.emptyGridTitle)
                    addBlocks()

                    val s = getString(R.string.now_the_puzzle_is) +
                            row + getString(R.string.row) +
                            col + getString(R.string.column)
                    toast(s)

                    //保存行列数
                    sharedPreferences.edit()
                        .putInt(SHARED_PREFERENCE_ROW, row)
                        .putInt(SHARED_PREFERENCE_COL, col)
                        .apply()
                }
            }
        }
    }

    /**
     * 刷新拼图的信息
     */
    private fun refreshPuzzleInformation() {
        for (row in 0..puzzle.row) {
            for (col in 0..puzzle.col - 1) {
                (layout_blocks.get(row * puzzle.col + col) as TextView)
                    .setText(
                        "${puzzle.getBlocks()[row][col]},\n$row${getString(R.string.row)} ${col + 1}${getString(
                            R.string.column
                        )}"
                    )
            }
        }
    }


    private inner class MyOnTouchListener : View.OnTouchListener {
        //最小移动，超过这个值才执行
        val minMove = 100
        //最大移动，超过这个值 不执行
        val maxMove = 100

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (v != null) {
                val gestureDetectorCompat =
                    GestureDetectorCompat(context, MyGestureListener(v as TextView))
                gestureDetectorCompat.onTouchEvent(event)

                when (event?.action) {
                    MotionEvent.ACTION_UP -> {
                        if (isLongPress && endX != startX && endY != startY) {
                            var moved = false
                            if (startY!! - endY!! > minMove && abs(endX!! - startX!!) < maxMove) {
                                //"上移"
                                moved = puzzle.moveUp()
                            } else if (endY!! - startY!! > minMove && abs(endX!! - startX!!) < maxMove) {
                                //"下移"
                                moved = puzzle.moveDown()
                            } else if (startX!! - endX!! > minMove && abs(endY!! - startY!!) < maxMove) {
                                //左移
                                moved = puzzle.moveLeft()
                            } else if (endX!! - startX!! > minMove && abs(endY!! - startY!!) < maxMove) {
                                //右移
                                moved = puzzle.moveRight()
                            }

                            showMoveResult(moved)
                            isLongPress = false
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        endX = event?.rawX
                        endY = event?.rawY
                    }
                }
            }
            return true
        }
    }

    private fun showMoveResult(moved: Boolean) {
        if (moved) {
            refreshPuzzleInformation()
            soundPlayer.play(R.raw.move_puzzle)

            //检查拼图是否成功
            if (puzzle.checkSuccessful()) {
                toast("恭喜你！拼图已经完成！你可以按右上角打乱顺序选项打乱顺序。")
                soundPlayer.play(R.raw.congratulation_puzzle)
            }
            //不能移动
        } else {
            soundPlayer.play(R.raw.unmove_puzzle)
        }
    }

    companion object {
        val EXTRA_ROW = "row"
        val EXTRA_COL = "col"

        private val SHARED_PREFERENCE_ROW = "puzzle_row"
        private val SHARED_PREFERENCE_COL = "puzzle_col"
        private val SHARED_PREFERENCE_IS_FIRST = "puzzle_is_first"

        private val KEY_ROW = 0
        private val KEY_COL = 1

        private val REQUEST_SET_ROW_COL = 1
    }


    private inner class MyGestureListener(private val textView: TextView) :
        GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return super.onSingleTapUp(e)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return super.onDown(e)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            return super.onDoubleTap(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onContextClick(e: MotionEvent?): Boolean {
            return super.onContextClick(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            return super.onSingleTapConfirmed(e)
        }

        override fun onShowPress(e: MotionEvent?) {
            super.onShowPress(e)
        }

        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            return super.onDoubleTapEvent(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            isLongPress = true
            val map: Map<Int, Int> = textView.tag as Map<Int, Int>
            puzzle.setFocusBlockRow(map.get(KEY_ROW)!!)
            puzzle.setFocusBlockCol(map.get(KEY_COL)!!)

            startX = e?.rawX
            startY = e?.rawY

            endX = startX
            endY = startY
        }


    }
}