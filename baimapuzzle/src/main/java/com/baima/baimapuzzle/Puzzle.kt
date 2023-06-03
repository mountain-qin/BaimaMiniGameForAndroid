package com.baima.baimapuzzle

import kotlin.random.Random

class Puzzle @JvmOverloads constructor(
    var row: Int = DEFAULT_ROW,
    var col: Int = DEFAULT_COL,
    val emptyGridTitle: String = "empty",
    order:Int= DISORDER
) {

    private lateinit var blocks: Array<Array<String>>
    //顺序 的方块数组，用于对照
    private lateinit var orderBlocks: Array<Array<String>>

    //空格子的行列
    private var emptyGridRow = 0
    private var emptyGridCol = 0
    //焦点方块的行列
    private var focusBlockRow = 0
    private var focusBlockCol = 0

    init {
        blocks = Array(row + 1, { Array(col, { "" }) })
        orderBlocks = Array(row + 1, { Array(col, { "" }) })

        for (r in 0..row - 1) {
            for (c in 0..col - 1) {
                blocks[r + 1][c] = (r * col + c + 1).toString()
                orderBlocks[r + 1][c] = (r * col + c + 1).toString()
            }
        }
        blocks[0][0] = emptyGridTitle
        orderBlocks[0][0] = emptyGridTitle

        if(order== DISORDER) {
            disruptOrder()
        }
    }


    fun getBlocks(): Array<Array<String>> {
        return blocks
    }

    fun setFocusBlockRow(row: Int) {
        focusBlockRow = row
    }


    fun setFocusBlockCol(col: Int) {
        focusBlockCol = col
    }


    fun moveUp(): Boolean {
        if (focusBlockCol == emptyGridCol && focusBlockRow > emptyGridRow) {
            //获取要交换数据的方块的行列坐标
            val rowColList = mutableListOf<Array<Int>>()
            for (i in 0..focusBlockRow - emptyGridRow) {
                rowColList.add(arrayOf(emptyGridRow + i, emptyGridCol))
            }

            exchangeData(rowColList, TYPE_FORWARD)
            emptyGridRow = focusBlockRow
            return true
        }
        return false
    }


    fun moveDown(): Boolean {
        if (focusBlockCol == emptyGridCol && focusBlockRow < emptyGridRow) {
            val rowColList = mutableListOf<Array<Int>>()
            for (i in 0..emptyGridRow - focusBlockRow) {
                rowColList.add(arrayOf(focusBlockRow + i, focusBlockCol))
            }
            exchangeData(rowColList, TYPE_BACKWARD)
            emptyGridRow = focusBlockRow
            return true
        }
        return false
    }


    fun moveLeft(): Boolean {
        //在同一行，焦点的列大于空格子的列
        if (focusBlockRow == emptyGridRow && focusBlockCol > emptyGridCol) {
            val rowColList = mutableListOf<Array<Int>>()
            for (i in 0..focusBlockCol - emptyGridCol) {
                rowColList.add(arrayOf(emptyGridRow, emptyGridCol + i))
            }

            exchangeData(rowColList, TYPE_FORWARD)
            emptyGridCol = focusBlockCol
            return true
        }
        return false
    }


    fun moveRight(): Boolean {
        if (focusBlockRow == emptyGridRow && focusBlockCol < emptyGridCol) {
            val rowColList = mutableListOf<Array<Int>>()
            for (i in 0..emptyGridCol - focusBlockCol) {
                rowColList.add(arrayOf(focusBlockRow, focusBlockCol + i))
            }
            exchangeData(rowColList, TYPE_BACKWARD)
            emptyGridCol = focusBlockCol
            return true
        }
        return false
    }


    /**
     * 交换数据
     * @param rowColList 行列坐标列表
     * @param type 交换的方式，有前进、后退；
     */
    private fun exchangeData(rowColList: MutableList<Array<Int>>, type: Int) {
        for (i in 0..rowColList.size - 1 - 1) {
            var rowCol = arrayOf<Int>()
            var rowCol1 = arrayOf<Int>()

            if (type == TYPE_FORWARD) {
                rowCol = rowColList.get(i)
                rowCol1 = rowColList.get(i + 1)
            } else if (type == TYPE_BACKWARD) {
                rowCol = rowColList.get(0)
                rowCol1 = rowColList.get(i + 1)
            }

            var temp = blocks[rowCol[0]][rowCol[1]]
            blocks[rowCol[0]][rowCol[1]] = blocks[rowCol1[0]][rowCol1[1]]
            blocks[rowCol1[0]][rowCol1[1]] = temp
        }
    }


    /**
     * 检查是否成功
     */
    fun checkSuccessful():Boolean{
        //和顺序的方块数组对照，都一样就成功；
        for (r in 0..row){
            for(c in 0..col-1){
                if(blocks[r][c]!=orderBlocks[r][c]){
                    return false
                }
            }
        }
        return true
    }


    /**
     * 打乱 顺序
     */
    fun disruptOrder(){
        var count=100
        while(count>0){
            var moved=false
            //在空格子所在的行找一个方块左右移动,排除第0行；
            if(Random.nextInt(2)==0 &&emptyGridRow!=0){
                val randomCol=Random.nextInt(col)
                //就是空格子，重新找
                if (randomCol==emptyGridCol){
                    continue
                }

                focusBlockRow=emptyGridRow
                focusBlockCol=randomCol

                if(randomCol<emptyGridCol){
                    moved=moveRight()
                }else if (randomCol>emptyGridCol){
                    moved=moveLeft()
                }
                //在空格子所在的列找一个方块上下移动；不找第0行的；
            }else{
                val randomRow=Random.nextInt(row)+1
                //就是空格子，重新找
if(randomRow==emptyGridRow){
    continue
}

                focusBlockRow=randomRow
                focusBlockCol=emptyGridCol

                if(randomRow<emptyGridRow){
                    moved=moveDown()
                }else if(randomRow>emptyGridRow){
                    moved=moveUp()
                }
            }

if(moved) {
    count -= 1
}
        }

        //顺序打乱了。向右移动第0列，再向下移动第0行；
        focusBlockRow=emptyGridRow
        focusBlockCol=0
        moveRight()

        focusBlockRow=0
        moveDown()

        emptyGridRow=0
        emptyGridCol=0
    }


    /**
     * 调整顺序
     */
    fun adjustOrder(){
        //重新生成有顺序的；
        val p =Puzzle(row = row, col=col, emptyGridTitle = emptyGridTitle,order= ORDER)
        blocks=p.getBlocks()
        orderBlocks=p.orderBlocks

        emptyGridRow=0
        emptyGridCol=0
    }


    companion object {
        private val TYPE_FORWARD = 0
        private val TYPE_BACKWARD = 1

        private val DISORDER=0
        private val ORDER=1

        val DEFAULT_ROW=3
        val DEFAULT_COL=3
            }
}