package com.github.mutoxu_n.sudoku

import android.util.Log
import java.io.InputStream

private const val DATA_SIZE = 83
fun choiceProblem(stream: InputStream): Int {
    val size = stream.available()
    val len = size / DATA_SIZE
    val idx = (Math.random() * len).toInt()
    Log.i("choiceProblem", "Index: $idx / $len")

    return idx
}

fun getProblemFromId(stream: InputStream, id: Int): String {
    val size = stream.available()
    val len = size / DATA_SIZE
    if(id >= len) return ""

    val reader = stream.bufferedReader()
    reader.skip(83L * id)

    val p = reader.readLine()
    Log.i("choiceProblem", "problem: $p")
    return p
}