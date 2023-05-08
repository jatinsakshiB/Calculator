package com.jsb.calculator.modules

data class MyFont(
    var a: String = "a",
    var b: String = "b",
    var c: String = "c",
    var d: String = "d",
    var e: String = "e",
    var f: String = "f",
    var g: String = "g",
    var h: String = "h",
    var i: String = "i",
    var j: String = "j",
    var k: String = "k",
    var l: String = "l",
    var m: String = "m",
    var n: String = "n",
    var o: String = "o",
    var p: String = "p",
    var q: String = "q",
    var r: String = "r",
    var s: String = "s",
    var t: String = "t",
    var u: String = "u",
    var v: String = "v",
    var w: String = "w",
    var x: String = "x",
    var y: String = "y",
    var z: String = "z",

    var bigA: String = "A",
    var bigB: String = "B",
    var bigC: String = "C",
    var bigD: String = "D",
    var bigE: String = "E",
    var bigF: String = "F",
    var bigG: String = "G",
    var bigH: String = "H",
    var bigI: String = "I",
    var bigJ: String = "J",
    var bigK: String = "K",
    var bigL: String = "L",
    var bigM: String = "M",
    var bigN: String = "N",
    var bigO: String = "O",
    var bigP: String = "P",
    var bigQ: String = "Q",
    var bigR: String = "R",
    var bigS: String = "S",
    var bigT: String = "T",
    var bigU: String = "U",
    var bigV: String = "V",
    var bigW: String = "W",
    var bigX: String = "X",
    var bigY: String = "Y",
    var bigZ: String = "Z",
){

    fun toQWERTYList(): List<String> = listOf(q, w, e, r, t, y, u, i, o, p, a, s, d, f, g, h, j, k, l, z, x, c, v, b, n, m)

    fun toABCDEFList(): List<String> = listOf(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z)

    fun toBigQWERTYList(): List<String> = listOf(bigQ, bigW, bigE, bigR, bigT, bigY, bigU, bigI, bigO, bigP, bigA, bigS, bigD, bigF, bigG, bigH, bigJ, bigK, bigL, bigZ, bigX, bigC, bigV, bigB, bigN, bigM)

    fun toBigABCDEList(): List<String> = listOf(bigA, bigB, bigC, bigD, bigE, bigF, bigG, bigH, bigI, bigJ, bigK, bigL, bigM, bigN, bigO, bigP, bigQ, bigR, bigS, bigT, bigU, bigV, bigW, bigX, bigY, bigZ)
}