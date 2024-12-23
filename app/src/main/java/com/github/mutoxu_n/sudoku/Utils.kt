package com.github.mutoxu_n.sudoku

import android.util.Log
import java.io.InputStream

private const val DATA_SIZE = 83
fun choiceProblem(stream: InputStream): String {
    val size = stream.available()
    val len = size / DATA_SIZE
//    val idx = (Math.random() * len).toInt()
    val idx = 0

    val reader = stream.bufferedReader()
    reader.skip(83L * idx)

    val p = reader.readLine()
    Log.i("choiceProblem", "Index: $idx / $len")
    Log.i("choiceProblem", "problem: $p")
    return p
}