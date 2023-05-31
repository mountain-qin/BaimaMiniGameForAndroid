package com.baima.baimapuzzle

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class Puzzle3x3InstrumentedTest {

    val puzzle3x3Blocks= arrayOf(
        arrayOf("empty","",""),
        arrayOf("1","2","3"),
        arrayOf("4","5","6"),
        arrayOf("7","8","9")
    )

    val puzzle5x4Blocks= arrayOf(
        arrayOf("empty","","",""),
        arrayOf("1","2","3","4"),
        arrayOf("5","6","7","8"),
        arrayOf("9","10","11","12"),
        arrayOf("13","14","15","16"),
        arrayOf("17","18","19","20")
    )

    @Test
    fun initTest(){
    val p=Puzzle()
        val blocks = p.getBlocks()
        for (r in 0..p.row){
            for(c in 0..p.col-1){
                assertEquals(puzzle3x3Blocks[r][c], blocks[r][c])
            }
        }

        val p1=Puzzle(row = 5,col = 4)
        val blocks1=p1.getBlocks()
        for (r in 0..p1.row){
            for(c in 0..p1.col-1){
                assertEquals(puzzle5x4Blocks[r][c], blocks1[r][c])
            }
        }
    }


    @Test
    fun moveUpTest(){
        val puzzle = Puzzle()
        puzzle.setFocusBlockRow(2)
        puzzle.setFocusBlockCol(0)
        puzzle.moveUp()
        val blocks = puzzle.getBlocks()

        val arr= arrayOf(
            arrayOf("1","",""),
            arrayOf("4","2","3"),
            arrayOf("empty","5","6"),
            arrayOf("7","8","9")
        )

        for(r in 0..puzzle.row){
            for(c in 0..puzzle.col-1){
                assertEquals(arr[r][c], blocks[r][c])
            }
        }

    }


    @Test
    fun moveLeftTest(){
        val puzzle = Puzzle()
        //4
        puzzle.setFocusBlockRow(2)
        puzzle.setFocusBlockCol(0)
        puzzle.moveUp()

        //5
        puzzle.setFocusBlockRow(2)
        puzzle.setFocusBlockCol(1)
        puzzle.moveLeft()

        val arr= arrayOf(
            arrayOf("1","",""),
            arrayOf("4","2","3"),
            arrayOf("5","empty","6"),
            arrayOf("7","8","9")
        )

        for (r in 0..puzzle.row){
            for(c in 0..puzzle.col-1){
                assertEquals(arr[r][c], puzzle.getBlocks()[r][c])
            }
        }

    }


    @Test
    fun moveDownTest(){
        val puzzle = Puzzle()
        //7
        puzzle.setFocusBlockRow(3)
        puzzle.setFocusBlockCol(0)
        puzzle.moveUp()

        //8
        puzzle.setFocusBlockRow(3)
        puzzle.setFocusBlockCol(1)
        puzzle.moveLeft()

        //2
        puzzle.setFocusBlockRow(1)
        puzzle.setFocusBlockCol(1)
        puzzle.moveDown()

        val arr= arrayOf(
            arrayOf("1","",""),
            arrayOf("4","empty","3"),
            arrayOf("7","2","6"),
            arrayOf("8","5","9")
        )

        for(r in 0..puzzle.row){
            for(c in 0..puzzle.col-1){
                assertEquals(arr[r][c], puzzle.getBlocks()[r][c])
            }
        }

    }


    @Test
    fun moveRightTest(){
        val puzzle = Puzzle()
        //7
        puzzle.setFocusBlockRow(3)
        puzzle.moveUp()

        //9
        puzzle.setFocusBlockCol(2)
        puzzle.moveLeft()

        //6
        puzzle.setFocusBlockRow(2)
        puzzle.moveDown()

        //2
        puzzle.setFocusBlockCol(0)
        puzzle.moveRight()

        val arr= arrayOf(
            arrayOf("1","",""),
            arrayOf("4","2","3"),
            arrayOf("empty","7","5"),
            arrayOf("8","9","6")
        )

        for(r in 0..puzzle.row){
            for(c in 0..puzzle.col-1){
                assertEquals(arr[r][c], puzzle.getBlocks()[r][c])
            }
        }

    }
}