package com.baima.baimapuzzle

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.get
import com.baima.baimapuzzle.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_puzzle.*
import org.jetbrains.anko.toast
import kotlin.math.abs

class PuzzleActivity : AppCompatActivity() {

    private var startX:Float?=null
    private var startY:Float?=null
    private var endX:Float?=null
    private var endY:Float?=null

    private var isLongPress=false

    private lateinit var context: Context
    private lateinit var puzzle: Puzzle
    private lateinit var layout_blocks:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.baima_puzzle)
        setContentView(R.layout.activity_puzzle)

        context = this
        puzzle = Puzzle(empty_grid_title = getString(R.string.empty))

        addBlocks()
    }

    /**
     * 添加方块
     */
    private fun addBlocks() {
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
                    textView.visibility=View.GONE
                }

                //1,1行 1列
                textView.setText("${blocks[row][col]},\n$row${getString(R.string.row)} ${col + 1}${getString(R.string.column)}")

                textView.x = blockWidth * col.toFloat()
                textView.y = blockHeight * row.toFloat()

                val layoutParams1 = RelativeLayout.LayoutParams(blockWidth, blockHeight)
                layoutParams1.setMargins(2, 2, 2, 2)

textView.tag= mapOf(KEY_ROW to row, KEY_COL to col)

                layout_blocks.addView(textView, layoutParams1)

                textView.setOnTouchListener(myOnTouchListener)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_puzzle_activity,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_adjust_order->{
                puzzle.adjustOrder()
                refreshPuzzleInformation()
                toast(R.string.order_adjusted)
            }
            R.id.item_disrupt_order->{
                puzzle.disruptOrder()
                refreshPuzzleInformation()
                toast(R.string.order_disrupted)
            }
        }
return true
    }


    /**
     * 刷新拼图的信息
     */
    private fun refreshPuzzleInformation() {
        for(row in 0..puzzle.row){
            for(col in 0..puzzle.col-1){
                (layout_blocks.get(row*puzzle.col+col) as TextView)
                    .setText("${puzzle.getBlocks()[row][col]},\n$row${getString(R.string.row)} ${col+1}${getString(R.string.column)}")
            }
        }
    }


    private inner class MyOnTouchListener:View.OnTouchListener{
        //最小移动，超过这个值才执行
        val minMove=100
        //最大移动，超过这个值 不执行
        val maxMove=100

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if(v!=null){
                val gestureDetectorCompat = GestureDetectorCompat(context, MyGestureListener(v as TextView))
                gestureDetectorCompat.onTouchEvent(event)

                when(event?.action){
                    MotionEvent.ACTION_UP-> {
                        if (isLongPress) {
                            var moved=false
                            if (startY!! -endY!! >minMove&& abs(endX!!-startX!!)<maxMove){
                                //"上移"
                                moved=puzzle.moveUp()
                            }else if(endY!!-startY!!>minMove&& abs(endX!!-startX!!)<maxMove){
                                //"下移"
                                moved=puzzle.moveDown()
                            }else if(startX!!-endX!!>minMove&& abs(endY!!-startY!!)<maxMove){
                                //左移
                                moved=puzzle.moveLeft()
                            }else if (endX!!-startX!!>minMove&& abs(endY!!-startY!!)<maxMove){
                                //右移
                                moved=puzzle.moveRight()
                            }

                            if(moved){
refreshPuzzleInformation()

                                //检查拼图是否成功
                                if(puzzle.checkSuccessful()){
                                    toast("恭喜你！拼图已经完成！你可以按右上角打乱顺序选项打乱顺序。")
                                }
                                //不能移动
                            }else{
                                toast("不能移动！")
                            }
                        }
                        isLongPress=false
                    }
                    MotionEvent.ACTION_MOVE->{
                        endX=event?.rawX
                        endY=event?.rawY
                    }
                }
            }
                return true
        }
    }

    companion object{
        private val KEY_ROW=0
        private val KEY_COL=1
    }


    private inner class MyGestureListener(private val textView: TextView): GestureDetector.SimpleOnGestureListener() {

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

            startX =e?.rawX
            startY =e?.rawY
        }


    }
}